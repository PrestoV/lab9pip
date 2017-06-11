new Vue({
    el: '#login_form',
    data: {
        login: '',
        password:'',
        message: '',
        hasWarning: false,
        hasError: false,
        incorrectLogin : false,
        incorrectPassword: false
    },
    methods: {
        isValidLogin: function () {
            this.incorrectLogin = this.login.length <= 0;
            return !this.incorrectLogin;
        },
        isValidPassword: function () {
            this.incorrectPassword = this.password.length <= 0;
            return !this.incorrectPassword;
        },
        submitForm: function () {
            if(this.isValidLogin() && this.isValidPassword()) {
                this.hasWarning = false;
                this.hasError = false;
                this.$http({url: '/', method: 'POST',
                    data: {
                        login: this.login,
                        password: this.password,
                        action: 'authorize'
                    }
                }).then(
                    function (response) {
                        this.hasError = false;
                        location.reload();
                    },
                    function (response) {
                        this.hasError = true;
                        this.message = response.data.error;
                    }
                );
            } else {
                this.hasWarning = true;
                this.hasError = false;
                this.message = "Поля обязательны к заполнению!"
            }
        }
    }
});