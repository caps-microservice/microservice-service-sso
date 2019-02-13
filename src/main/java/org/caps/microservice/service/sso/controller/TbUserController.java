package org.caps.microservice.service.sso.controller;

import org.apache.commons.lang.StringUtils;
import org.caps.microservice.common.domain.TbUser;
import org.caps.microservice.common.dto.BaseResult;
import org.caps.microservice.common.utils.CookieUtils;
import org.caps.microservice.common.utils.MapperUtils;
import org.caps.microservice.service.sso.service.LoginService;
import org.caps.microservice.service.sso.service.consumer.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@Controller
public class TbUserController {
    @Autowired
    private LoginService userService;
    @Autowired
    private RedisCacheService redisCacheService;
    /**
     * 跳转登录页
     *
     * @return
     */
    @RequestMapping(value = {"login",""}, method = RequestMethod.GET)
    public String login(
            @RequestParam(required = false) String url,
            HttpServletRequest request, Model model) {
        String token = CookieUtils.getCookieValue(request, "token");

        // token 不为空可能已登录
        if (StringUtils.isNotBlank(token)) {
            String loginCode = redisCacheService.get(token);
            if (StringUtils.isNotBlank(loginCode)) {
                String json = redisCacheService.get(loginCode);
                if (StringUtils.isNotBlank(json)) {
                    try {
                        TbUser tbUser = MapperUtils.json2pojo(json, TbUser.class);
                        // 已登录
                        if (tbUser != null) {
                            if (StringUtils.isNotBlank(url)) {
                                return "redirect:" + url;
                            }
                        }
                        // 将登录信息传到登录页
                        model.addAttribute("tbSysUser", tbUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

       if (StringUtils.isNotBlank(url)) {
            model.addAttribute("url", url);
        }

        return "user/login";
    }
    /**
     * 处理登陆业务
     * @param phone
     * @param password
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(
            @RequestParam(required = true) String phone,
            @RequestParam(required = true) String password,
            @RequestParam(required = false) String url,
            HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes
    ){
        TbUser tbUser = userService.login(phone, password);
        if (tbUser==null){
            //因为是重定向，数据无法传送到要重定向的页面，可以通过RedirectAttributes把数据以参数的形式传送
            //1. addAttribute方法传递参数会跟随在URL后面 ，如上代码即为http:/localhost/login?message=用户名或密码错误，请重新登录
            //2. 使用addFlashAttribute不会跟随在URL后面，会把该参数值暂时保存于session，
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误，请重新登录");
        }
        else {
            String token = UUID.randomUUID().toString();
            //将token放入缓存
            String result = redisCacheService.put(token, phone, 60 * 60 * 24);
            if (StringUtils.isNotBlank(result) && "ok".equals(result)) {
                CookieUtils.setCookie(request, response, "token", token, 60 * 60 * 24);
                if (StringUtils.isNotBlank(url)) {
                    return "redirect:" + url;
                }
            }
            //熔断处理
            else {
                redirectAttributes.addFlashAttribute("服务器异常，请稍后重试");
            }
        }
        return "redirect:/login";
    }
}
