const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
};

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$.ajaxSetup({
    converters: {
        "text json": function (stringData) {
            var json = JSON.parse(stringData);
            if (typeof json === 'object') {
                $(json).each(function () {
                    if (this.hasOwnProperty('dateTime')) {
                        this.dateTime = this.dateTime.substr(0, 16).replace('T', ' ');
                    }
                });
            }
            return json;
        }
    }
});

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
                    },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "render": renderEditBtn,
                    "defaultContent": "",
                    "orderable": false
                },
                {
                    "render": renderDeleteBtn,
                    "defaultContent": "",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                        $(row).attr("data-meal-excess", data.excess);
            }
        }));
    
    $.datetimepicker.setLocale(localeCode);
    //  http://xdsoft.net/jqplugins/datetimepicker/
        var startDate = $('#startDate');
        var endDate = $('#endDate');
        startDate.datetimepicker({
            dayOfWeekStart: 1,
            timepicker: false,
            format: 'Y-m-d',
            formatDate: 'Y-m-d',
            onShow: function (ct) {
                this.setOptions({
                    maxDate: endDate.val() ? endDate.val() : false
                })
            }
        });
        endDate.datetimepicker({
            dayOfWeekStart: 1,
            timepicker: false,
            format: 'Y-m-d',
            formatDate: 'Y-m-d',
            onShow: function (ct) {
                this.setOptions({
                    minDate: startDate.val() ? startDate.val() : false
                })
            }
        });
    
        var startTime = $('#startTime');
        var endTime = $('#endTime');
        startTime.datetimepicker({
            datepicker: false,
            format: 'H:i',
            onShow: function (ct) {
                this.setOptions({
                    maxTime: endTime.val() ? endTime.val() : false
                })
            }
        });
        endTime.datetimepicker({
            datepicker: false,
            format: 'H:i',
            onShow: function (ct) {
                this.setOptions({
                    minTime: startTime.val() ? startTime.val() : false
                })
            }
        });
    
        $('#dateTime').datetimepicker({
            dayOfWeekStart: 1,
            format: 'Y-m-d H:i'
        });
})