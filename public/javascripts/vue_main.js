new Vue({
    el: '#unauthorize_button',
    methods: {
        unauthorize: function () {
            this.$http({url: '/', method: 'POST',
                data: {
                    action: 'unauthorize'
                }
            }).then(
                function (response) {
                    window.location = "/";
                }
            );
        }
    }
});

var rParam =  new Vue({
    el: '#r_div',
    data: {
        rElement: null,
        incorrectR: false
    },
    methods: {
        selectR: function(event) {
            if(this.rElement !== null) {
                this.rElement.style.backgroundColor = 'white';
            }
            if(this.rElement !== event.currentTarget) {
                this.rElement = event.currentTarget;
                this.rElement.style.backgroundColor = 'lightslategray';
            } else {
                this.rElement = null;
            }
        },
        getR: function() {
            return this.isValidR() ? this.rElement.innerHTML : null;
        },
        isValidR: function() {
            return this.rElement !== null;
        }
    }
});

var pointParam = new Vue({
    el: '#point_div',
    data: {
        xElement: null,
        yValue: null,
        incorrectCoords: false,
        hasWarning: false,
        message: ''
    },
    methods: {
        selectX: function(event) {
            if(this.xElement !== null) {
                this.xElement.style.backgroundColor = 'white';
            }
            if(this.xElement !== event.currentTarget) {
                this.xElement = event.currentTarget;
                this.xElement.style.backgroundColor = 'lightslategray';
            } else {
                this.xElement = null;
            }
        },
        check: function() {
            var x = this.getX();
            var y = this.getY();
            var r = rParam.getR();

            this.hasWarning = false;
            rParam.incorrectR = false;
            this.incorrectCoords = false;

            if(x !== null && y !== null && r !== null) {
                this.$http({url: '/addpoint', method: 'GET',
                    data: {
                        x: x,
                        y: y,
                        r: r
                    }
                }).then(
                    function (response) {
                        alert(response.data);
                    },
                    function (response) {
                        alert("Произошла ошибка при обработке запроса.");
                    }
                );
            } else {
                this.message = "Укажите корректно параметры:";
                if(r === null) {
                    rParam.incorrectR = true;
                    this.message += " R";
                }
                if(x === null) {
                    this.incorrectCoords = true;
                    this.message += " X";
                }
                if(y === null) {
                    this.incorrectCoords = true;
                    this.message += " Y";
                }
                this.hasWarning = true;
            }
        },
        getX: function() {
            return this.isValidX() ? this.xElement.innerHTML : null;
        },
        getY: function() {
            return this.isValidY() ? this.yValue : null;
        },
        isValidX: function() {
            return this.xElement !== null;
        },
        isValidY: function() {
            if(this.yValue === null) {
                return false;
            }
            this.yValue.trim();
            var value = this.trimValue(this.yValue);
            return !isNaN(parseFloat(value))
                && +value >= -3
                && +value <= 3;
        },
        trimValue: function(number) {
            return number.replace(/[.]00+/, '.0');
        }
    }
});

var coordPlot = new Vue({
    methods: {
        drawPlot: function () {
            var canvasContext = document.getElementById("plot").getContext("2d");
            canvasContext.clearRect(0, 0, canvasContext.width, canvasContext.height);

            var image = new Image();
            image.src = "/assets/images/area.png";
            image.onload = function () {
                canvasContext.drawImage(image, 0, 0);
            }
        }
    }
});

window.onload = coordPlot.drawPlot();