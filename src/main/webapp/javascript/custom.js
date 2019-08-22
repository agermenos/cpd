$.makeTable = function (mydata) {
    var table = $("<table id=\"response-table\" class=\"table table-condensed table-striped table-sm\">");
    var tblHeader = "<thead><tr>";
    for (var k in mydata[0]) {
        if (k!=="id" && k!=="altIdStr") tblHeader += "<th>" + k + "</th>";
    }
    tblHeader += "</tr><tbody>";
    $(tblHeader).appendTo(table);
    $.each(mydata, function (index, value) {
        var TableRow = "<tr>";
        $.each(value, function (key, val) {
            if (key==="id" || key==="altIdStr") {
                TableRow += "<td style='display:none'>" + val + "</td>";
            }
            else {TableRow += "<td>" + val + "</td>";}

        });
        TableRow += "</tr>";
        $(table).append(TableRow);
    });
    $(table).append("</tbody>");
    return ($(table));
};

var flattenString = function (textArray) {
    var returnString=[];
    for (var index in textArray){
        var text = textArray[index];
        returnString.push(text.slice(0, text.length-3));
    }
    return returnString.toString();
};

$(function () {
    function chooseColor(queryType) {
        switch (queryType) {
            case 'must':return "list-group-item-success";
            case 'should':return "list-group-item-secondary";
            case 'not':return "list-group-item-danger";
        }
    }

    $('#query-adder').on('click', function () {
        var list = $('#query-list');
        var queryName = $('#query-field').val();
        var queryType = $('#query-type').val();
        var querySubType = $('#query-subtype').val();
        var queryValue = $('#query-value').val();
        list.show();
        list.append("" +
            "<li class=\"list-group-item " + chooseColor(queryType) + " d-flex justify-content-between align-items-center\">" +
            queryName + ":" + queryType + ":" + querySubType + ":" + queryValue +  "<button align=\"right\" type=\"button\" class=\"btn btn-primary btn-sm\" onclick='$(this).closest(\"li\").remove();'>-</button>\n </li>"
        );
    });

    var executeBooleanQuery = function () {
        var musts=[];
        var nots=[];
        var shoulds=[];
        $('#query-list').children('li').each(function() {
            var query = this.textContent.split(":");
            switch (query[1]){
                case 'must':musts.push(query[0]+":" + query[2] + "="+query[3]);break;
                case 'should':shoulds.push(query[0]+":" + query[2] + "="+query[3]);break;
                case 'not':nots.push(query[0]+":" + query[2] + "="+query[3]);break;
            }
        });
        var requestBody = {};
        requestBody.musts = flattenString(musts);
        requestBody.nots = flattenString(nots);
        requestBody.shoulds= flattenString(shoulds);
        requestBody.indexName="products";
        $.ajax({
            type:"GET",
            dataType:"json",
            data:requestBody,
            url:"http://localhost:8080/demo/match/"
        }).
        done(function(data) {
            $("#response-table tr").remove();
            var table = $.makeTable(data);
            $("#response-table").replaceWith($(table));
        });
    };

    var executeRegexpQuery = function () {
        alert("hola, mundo");
    };

    $('#query-execution').on('click', function(){
            var ref_this = $('ul#tabs li a.active')[0]['id'];
            if (ref_this==='query') executeBooleanQuery();
            if (ref_this==='regexp') executeRegexpQuery();
    });

    $('#badge').on('click', function(){
       var value=(this.value());
    });
});