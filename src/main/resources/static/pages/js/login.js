let vLogin = new Vue({
    el: '#login',
    data: function () {
        return {
            /*seen: false,*/
            loginError: '',//错误提示信息
            phone: '',
            password: '',
            /*accountForm: {username: '', password: ''},//账户表单属性*/
            accountRules: {
                phone: [{required: true, message: '账号不能为空', trigger: 'blur'}],
                password: [{required: true, message: '密码不能为空', trigger: 'blur'}],
            },
        }
    },
    methods: {
        loginSubmit(){
            axios.post('/login/?phone='+vLogin.phone+'&password='+vLogin.password
            ).then(res => {
                console.log(res);
                if(res.data.status=="200"){
                    alert("登陆成功!");
                    window.location.href = "/newPage/";
                }
                else {
                    alert(res.data.message);
                    this.password = '';
                    this.phone = '';
                }
            }
            )
        }
    }
});


