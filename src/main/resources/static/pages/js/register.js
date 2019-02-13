let vLogin = new Vue({
    el: '#register',
    data: function () {
        return {
            content: '发送验证码',
            totalTime: 60,
            canClick: true,//添加canClick
            seen: false,
            validateSeen:false,
            phvalidateSeen:true,
            phvalidate:'',
            validate:"",
            loginError: '',//错误提示信息
            verification:'',
            accountForm: {password: '',phone:''},
        }
    },
    methods: {
        //注册事件
        registerSubmit(){
            let validate=vLogin.validate;
            let url='/register/?phone='+vLogin.accountForm.phone+'&password='+vLogin.accountForm.password+'&phvalidate='+vLogin.phvalidate ;
            console.log(validate);
            let that=this;
            axios.post(url
            ).then(res => {
                /*console.log(res);*/
                //如果返回500，把错误信息弹出
                if(res.data.status=="500"){
                    alert(res.data.message);
                    //如果是验证码错误，把手机验证码输入框隐藏，把图形验证码显示
                    if(res.data.message==="验证码错误"){
                        that.phvalidate="";
                        that.phvalidateSeen=false;
                        that.validateSeen=true;
                    }
                }
                else {
                    alert("注册成功");
                    window.location.href = "/loginPage/"
                }
            }
            ).catch(error=> {
                alert("服务器错误");
            });
        },
        //图形验证码60秒事件
        getPhValidate(){
            if (!this.canClick) return;
            this.canClick = false;
            this.content = this.totalTime + 's后重新发送';
            let clock = window.setInterval(() => {
                this.totalTime--;
                this.content = this.totalTime + 's后重新发送';
                if (this.totalTime < 0) {
                    window.clearInterval(clock);
                    this.content = '重新发送验证码';
                    this.totalTime = 60;
                    this.canClick = true  //这里重新开启
                }
            },1000);
            let url='/phonevalidate/';
            axios.get(url
            ).then(res=> {
                alert(res.data.data);
            }).catch(err=> {
                console.log(err);
            });
        },
        enter(){
            let url='/isvalidate/';
            var that=this;
            axios.get(url,{params: {validate: this.validate}}
            ).then(res=> {
                /*console.log(res)*/
                if(res.data.status===200){
                    that.phvalidateSeen=true;
                    that.validateSeen=false;
                }else {
                    alert("您的验证码位:"+res.data.message)
                }
            }
            ).catch(err=> {
                alert("服务器错误");
            });
        }

    }
});
function changeImg(obj){
    obj.src="/getImage?time="+new Date().getTime();
}

//禁止f5和鼠标右击刷新
document.onkeydown = function (e) {
    let ev = window.event || e;
    let code = ev.keyCode || ev.which;
    if (code == 116) {
        ev.keyCode ? ev.keyCode = 0 : ev.which = 0;
        cancelBubble = true;
        return false;
    }
}; //禁止f5刷新
document.oncontextmenu=function(){return false};//禁止右键刷新
