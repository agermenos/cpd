$.makeTable = function (mydata) {
    var table = $('<table border=1>');
    var tblHeader = "<tr>";
    for (var k in mydata[0]) tblHeader += "<th>" + k + "</th>";
    tblHeader += "</tr>";
    $(tblHeader).appendTo(table);
    $.each(mydata, function (index, value) {
        var TableRow = "<tr>";
        $.each(value, function (key, val) {
            TableRow += "<td>" + val + "</td>";
        });
        TableRow += "</tr>";
        $(table).append(TableRow);
    });
    return ($(table));
};

$(function () {
    function chooseColor(queryType) {
        switch (queryType) {
            case 'must':return "list-group-item-success";
            case 'should':return "list-group-item-secondary";
            case 'not':return "list-group-item-danger";
        };
    }

    $('#query-adder').on('click', function () {
        $('#query-list').show();
        var queryName = $('#query-field').val();
        var queryType = $('#query-type').val();
        var queryValue = $('#query-value').val();
        $('#query-list').append("" +
            "<li class=\"list-group-item " + chooseColor(queryType) + " d-flex justify-content-between align-items-center\">" +
                    queryName+":"+queryType+":"+queryValue +
                "<button align=\"right\" type=\"button\" class=\"btn btn-primary btn-sm\" onclick='$(this).closest(\"li\").remove();'>-</button>\n" +
            " </li>"
        );
    });

    $('#query-execution').on('click', function(){
        var musts=[];
        var nots=[];
        var shoulds=[];
        $('#query-list').children('li').each(function() {
            var query = this.textContent.split(":");
            switch (query[1]){
                case 'must':musts.push(query[0]+"="+query[2]);break;
                case 'should':shoulds.push(query[0]+"="+query[2]);break;
                case 'not':nots.push(query[0]+"="+query[2]);break;
            }
        })
        var requestBody = new Object();
        requestBody.musts = musts.toString().replace("-\n","").trim();
        requestBody.nots = nots.toString().replace("-\n","").trim();
        requestBody.shoulds=shoulds.toString().replace("-\n","").trim();
        requestBody.indexName="products";
        $.ajax({
            type:"GET",
            dataType:"json",
            data:requestBody,
            url:"http://localhost:8080/demo/match/"
        }).
        done(function(data) {
            var wrapper = new Object();
            wrapper.rows = data;
            var table = $.makeTable(data);
            $(table).appendTo("#response-table");
        });
    });
});