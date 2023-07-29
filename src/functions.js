function getValueFromTable(integrationId, spreadsheetId, sheetName, cells) {
    return $integration.googleSheets.readDataFromCells(
                integrationId,
                spreadsheetId,
                sheetName,
                [cells])[0].value.replace(']','').replace('[', '');
}


function generateRandomCells(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min)) + min;
}

function generateRandomPage () {
    var randNumber = generateRandomCells(0, 6);
    switch(randNumber) {
        case 0: 
            return 'RFL';
        case 1:
            return 'RUL';
        case 2:
            return 'RFD';
        case 3:
            return 'IFL';
        case 4:
            return 'IUL';
        case 5:
            return 'IFD';
    }
}

function getSizePage (integrationId, spreadsheetId, sheetName) {
    return getValueFromTable(integrationId, spreadsheetId, sheetName, 'A1');
}

function getUrlImage (sheetName) {
    var integrationId = $secrets.get("integrationId", "Токен не найден");
    var spreadsheetId = $secrets.get("spreadsheetId", "Токен не найден");
    var size = getSizePage(integrationId, spreadsheetId, sheetName);
    return getValueFromTable(integrationId, spreadsheetId, sheetName, size);
}





