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
                this.$http({url: '/getpoints', method: 'GET',
                    data: {
                        r: this.getR()
                    }
                }).then(
                    function (response) {
                        coordPlot.drawPlot(response.data);
                    },
                    function (response) {
                        alert("Произошла ошибка при обработке запроса.");
                    }
                );
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
                        coordPlot.drawPlot(response.data);
                        this.yValue = null;
                        this.xElement.style.backgroundColor = 'white';
                        this.xElement = null;
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
    el: '#plot',
    methods: {
        drawPlot: function (points) {
            var canvasContext = this.$el.getContext("2d");
            canvasContext.clearRect(0, 0, canvasContext.width, canvasContext.height);

            var image = new Image();
            image.src = "/assets/images/area.png";
            image.onload = function () {
                canvasContext.drawImage(image, 0, 0);
                if(points === null) {
                    coordPlot.$http({url: '/getpoints', method: 'GET', data: {}
                    }).then(
                        function (response) {
                            coordPlot.drawPoints(response.data);
                        },
                        function (response) {
                            alert("Произошла ошибка при обработке запроса.");
                        }
                    );
                } else {
                    coordPlot.drawPoints(points);
                }
            }
        },
        drawPoint: function (point) {
            var rd = (+point.r) / 154;
            var canvasRect = this.$el.getBoundingClientRect();

            var xCoord = (point.x / rd) + canvasRect.width / 2;
            var yCoord = canvasRect.height / 2 - (point.y / rd);

            this.$el.getContext("2d").fillStyle = point.inarea ? "#00FF00" : "#FF0000";
            this.$el.getContext("2d").fillRect(xCoord, yCoord, 3, 3);
        },
        drawPoints: function (jsonPoints) {
            for (let i in jsonPoints) {
                this.drawPoint(
                    { x: jsonPoints[i].x, y: jsonPoints[i].y, r: jsonPoints[i].r, inarea: jsonPoints[i].inarea }
                );
            }
            resultTable.fillTable();
        },
        plotClick: function (e) {
            var r = rParam.getR();
            if(r === null) {
                pointParam.message = "Укажите корректно параметры: R";
                pointParam.hasWarning = true;
                return;
            }
            pointParam.message = '';
            pointParam.hasWarning = false;

            var rd = r / 154;
            var canvasRect = this.$el.getBoundingClientRect();

            var x = (e.clientX - canvasRect.left - canvasRect.width / 2) * rd;
            var y = (canvasRect.height / 2 - e.clientY + canvasRect.top) * rd;

            x = x.toFixed(2);
            y = y.toFixed(2);

            this.$http({url: '/addpoint', method: 'GET',
                data: {
                    x: x,
                    y: y,
                    r: r
                }
            }).then(
                function (response) {
                    coordPlot.drawPlot(response.data);
                },
                function (response) {
                    alert(response.data.error);
                }
            );
        }
    }
});

var resultTable = new Vue({
    el: '#result_div',
    data: {
        showTable: false
    },
    methods: {
        fillTable: function() {
            this.showTable = true;
            this.$http({url: '/getpoints', method: 'GET', data: {}
            }).then(
                function (response) {
                    var table = document.getElementById("result_table_body");
                    table.innerHTML = "";
                    for(var i = Object.keys(response.data).length-1; i >= 0; i--) {
                        var tr = document.createElement('TR');

                        var tdX = document.createElement('TD');
                        var tdY = document.createElement('TD');
                        var tdR = document.createElement('TD');
                        var tdInArea = document.createElement('TD');
                        tdX.innerHTML = response.data[i].x;
                        tdY.innerHTML = response.data[i].y;
                        tdR.innerHTML = response.data[i].r;
                        tdInArea.innerHTML = response.data[i].inarea ? "да" : "нет";

                        tr.appendChild(tdX);
                        tr.appendChild(tdY);
                        tr.appendChild(tdR);
                        tr.appendChild(tdInArea);

                        table.appendChild(tr);
                    }
                    this.showTable = Object.keys(response.data).length !== 0;
                },
                function (response) {
                    alert("Произошла ошибка при обработке запроса.");
                }
            );
        }
    }
});

window.onload = coordPlot.drawPlot(null);