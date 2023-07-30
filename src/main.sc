require: requiments.sc
theme: /

    state: Start
        q!: $regex</start>
        a: Здравствуйте, я могу вам помочь выбрать пиво на вечер.
        script:
            delete $session.russianOrImport;
            delete $session.filterOrUnfilter;
            delete $session.darkOrLight;
            delete $session.sheetName;
        go!: /Main
    
    state: Main
        if: !$session.russianOrImport
            go!: /RussianOrImport
        elseif: !$session.filterOrUnfilter
            go!: /FilterOrUnfilter
        elseif: !$session.darkOrLight
            go!: /DarkOrLight
        else:
            script:
                $session.sheetName = $session.russianOrImport + $session.filterOrUnfilter + $session.darkOrLight;
            go!: /Check
        
    
    state: RussianOrImport
        q!: * $beer *
        q!: * {$advise * $beer} *
        q!: * {$help * $сhoose * $beer} *
        a: Вы больше предпочитаете российское или импорт?
        
        state: Russian
            q: * $first *
            q!: * $russian *
            script:
                $session.russianOrImport = 'R';
            go!: /Main
        
        state: Import
            q: * $second *
            q!: * $import *
            script:
                $session.russianOrImport = 'I';
            go!: /Main
    
    
    state: FilterOrUnfilter
        a: Фильтрованное или нефильтрованное?
        
        state: Filter
            q: * $first *
            q!: * $filter *
            script:
                $session.filterOrUnfilter = 'F';
            go!: /Main
                
        state: Unfilter
            q: * $second *
            q!: * $unfilter *
            script:
                $session.filterOrUnfilter = 'U';
                $session.darkOrLight = 'L';
            go!: /Main
        
    state: DarkOrLight
        a: Темное или светлое?
        state: Dark
            q: * $first *
            q!: * $dark *
            script:
                $session.darkOrLight = 'D';
            go!: /Main    
        state: Light
            q: * $second *
            q!: * $light *
            script:
                $session.darkOrLight = 'L';
            go!: /Main
    
    state: RussianFilterLight
        q!: * {$russian * $filter * $light} *
        script:
            $session.russianOrImport = 'R';
            $session.filterOrUnfilter = 'F';
            $session.darkOrLight = 'L';
        go!: /Main
    state: ImportFilterLight
        q!: * {import * $filter * $light} *
        script:
            $session.russianOrImport = 'I';
            $session.filterOrUnfilter = 'F';
            $session.darkOrLight = 'L';
        go!: /Main
  
    state: RussianUnfilterLight
        q!: * {$russian * $unfilter * $light} *
        script:
            $session.russianOrImport = 'R';
            $session.filterOrUnfilter = 'U';
            $session.darkOrLight = 'L'; 
        go!: /Main
    state: ImportUnfilterLight
        q!: * {$import * $unfilter * $light} *
        script:
            $session.russianOrImport = 'I';
            $session.filterOrUnfilter = 'U';
            $session.darkOrLight = 'L'; 
        go!: /Main
   
    state: RussianFilterDark
        q!: * {$russian * $dark} *
        q!: * {$russian * $filter * $dark} *
        script:
            $session.russianOrImport = 'R';
            $session.filterOrUnfilter = 'F';
            $session.darkOrLight = 'D';
        go!: /Main
    state: ImportFilterDark
        q!: * {$import * $dark} *
        q!: * {$import * $filter * $dark} *
        script:
            $session.russianOrImport = 'I';
            $session.filterOrUnfilter = 'F';
            $session.darkOrLight = 'D';
        go!: /Main

        
    state: IDidntUnderstand
        q!: *
        a: Я не совсем тебя понимаю... Попробуй переформулировать.
    
    state: AnyBeer
        q!: * $any *
        q!: * $recommend *
        script:
            $session.sheetName = generateRandomPage();
        go!: /Check
    
    state: Check
        if: $session.sheetName
            go!: /Answer
        else:
            a: Что-то пошло не так..
        
    state: Answer
        script:
            $temp.img = getUrlImage ($session.sheetName);
        if: $context.response.googleSheets.result === 'success'
            script:
                $response.replies = $response.replies || [];
                $response.replies.push({
                    "type": "image",
                    "imageUrl": $temp.img,
                    "text": "Изображение" // Это поле опционально.
                });
        else:
            a: Произошла ошибка