setupPage();

// marketplace functions:
function setupPage(){
    const cryptoSymbol = $("#cryptoSymbol").text("BTC");
    $("#cryptoOverview").append(cryptoSymbol);

    const cryptoTable = $("#cryptoTable");
    $(cryptoTable).append("<tr><th>Seller</th><th>Units for sale</th><th>Price per unit</th></tr>");
    $("#cryptoOverview").append(cryptoTable);
}
