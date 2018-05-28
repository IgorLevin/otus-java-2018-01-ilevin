function setCacheState(){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState === XMLHttpRequest.DONE && this.status == 200) {
            document.getElementById('cacheSize').innerHTML = JSON.parse(this.responseText).size;
            document.getElementById('miss').innerHTML = JSON.parse(this.responseText).miss;
            document.getElementById('hit').innerHTML = JSON.parse(this.responseText).hit;
        }
    };
    xhttp.open("GET", "/cache_state", true);
    xhttp.send();
}

function refresh() {
    setCacheState();
}