$(function(){

    // add event handler for user add functionality
    $("form#adduser").submit(function() {
        var name = $(this).find('input');

        console.log('add user clicked');
        if (name.val().length > 0) {
            addUser(name.val());
            name.val('');
        }
        return false;
    });

    // load inital users
    loadMembers();
});

function renderMembers(data) {
    data.forEach(function(entry) {
        panel = $("#users *[data-name='"+entry.name+"']");
        if (!panel.length) {
            appendMember(entry);
        }
    });
}

function appendMember(entry) {

    if ($("#users *[data-name='"+entry.name+"']").length) {
        return;
    }

    $("#users").append('<div data-name="'+entry.name+'" class="panel panel-default">' +
        '<div class="panel-heading"><h3>'+ entry.name + '</h3></div>' +
            '<div class="panel-body">' +
                '<div class="form-inline">' +
                    '<input type="text" placeholder="country" class="form-control" />' +
                    '<input type="text" placeholder="city" class="form-control" />' +
                    '<button type="button" class="btn btn-success">+</button>' +
                '</div>' +
            '</div>' +
        '</div>' +
        '</div>');

    node = $("#users").find("*[data-name='" + entry.name + "']");

    // render weather information for every city
    entry.locations.forEach(function (location) {
        addLocation(node, location.country, location.city)
    })

    node.find("h4[data-city][data-country]").each(function() {
        retieveCurrentWeather($(this))
    })

    // register handler for adding cities
    node.find("button").click(function(){
        node = $(this).parents('.panel');
        country = $(this).parent().find("input[placeholder='country']")
        city = $(this).parent().find("input[placeholder='city']")
        if (country.val().length > 0 && city.val().length > 0) {
            $.ajax({
                url: '/members/'+ entry.name +'/locations',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({"country":country.val(), "city":city.val()}),
                success: function() {
                    addLocation(node, country.val(), city.val())
                    country.val('');
                    city.val('');
                    retieveCurrentWeather(node.find("h4[data-country][data-city]:last"))
                }
            });
        }

    })
}

function addLocation(node, country, city) {
    node.find("div.form-inline").before('<div><h4 data-city="'+city+'" data-country="'+country+'" class="media-heading">' + city + ', ' + country + '</h4></div>');
}

function retieveCurrentWeather(node) {
    $.ajax({
        url: '/weather/now/'+node.data('country')+'/'+node.data('city'),
        dataType: 'json',
        success: function(data) {
            node.after('<div style="overflow: scroll"><img src="http://openweathermap.org/img/w/' +
                data.weatherIcon + '.png" />'+ data.temperature.toFixed(1) + '°C <button data-id="forecast" class="btn">forecast</button></div>');
            node.find("~ div:first").find("button.btn").click(function(){
                retrieveWeatherForecast($(this))
            })
        }
    });
}

function retrieveWeatherForecast(button) {
    loc = button.parent().parent().find("*[data-city][data-country]");

    $.ajax({
        url: '/weather/forecast/'+loc.data('country')+'/'+loc.data('city'),
        dataType: 'json',
        success: function(data) {
            console.log(data);
            data.entries.forEach(function(element, i){
                if ((i+1) % 8 == 0) {
                    button.parent().append(' | <img src="http://openweathermap.org/img/w/'+element.weatherIcon+'.png" />'+element.temperature+'°C');
                }
            });
            button.remove();
        }
    });
}

function loadMembers() {
    $.ajax({
        url: '/members',
        dataType: 'json',
        success: function(data) {
            renderMembers(data);
        }
    });
}

function addUser(name) {
    $.ajax({
        url: '/members',
        type: 'POST',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify({"name": name, "locations": []}),
        complete: function(data) {
            loadMembers();
        }
    });
}