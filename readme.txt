Rozproszona Baza Danych

Aby uruchomić węzeł bazodanowy:
java DatabaseNode -tcpport <numer portu TCP> -record <klucz>:<wartość> [ -connect <adres>:<port> ]

Aby uruchomić klienta:
java DatabaseClient -gateway <adres>:<numer portu TCP> -operation <operacja z parametrami>

Po uruchomieniu węzła z odpowiednimi parametrami:
1.  zostaje włączony serwer TCP nasłuchujący na porcie podanym przy argumencie -tcpport. Jest wtedy uruchamiany
    ServerAcceptThread jako osobny wątek. Obsługuje on ustanawianie nowych połączeń na serwerze TCP.
2.  przypisywane są klucz i wartość zgodnie z wartościami podanymi przy argumencie -record
3.  węzeł łączy się z innymi węzłami zgodnie z argumentem/ami -connect i zapisuje informację o połączeniu w hashmapie.
    Hashmapa przechowuje jako klucz String z adresem połączonego węzła oraz jako wartość obiekt klasy NodeConnection,
    która to z kolei zawiera Socket połączony z owym węzłem

Węzły tworzą wspólnie topologię siatki (każdy węzeł może się łączyć z wieloma innymi węzłami)

Węzły obsługują różne polecenia:
set-value <klucz>:<wartość> : ustawienie nowej wartości (drugi parametr) dla klucza będącego pierwszym parametrem.
    Wynikiem operacji jest komunikat OK jeśli operacja się powiodła lub ERROR jeśli baza nie zawiera żadnej pary,
    w której występuje żądany klucz
get-value <klucz> : pobranie wartości dla klucza będącego parametrem w bazie. Wynikiem operacji jest komunikat
    składający się z pary <klucz>:<wartość> jeśli operacja się powiodła lub ERROR jeśli baza nie zawiera żadnej pary,
    w której występuje żądany klucz.
find-key <klucz> : zlecenie wyszukania adresu i numeru portu węzła, na którym przechowywany jest rekord o zadanym
    kluczu. Jeśli taki węzeł istnieje, odpowiedzią jest para postaci <adres>:<port> identyfikująca ten węzeł
    lub komunikat ERROR jeśli żaden węzeł takiego klucza nie posiada
get-max : znalezienie największej wartości przypisanej do wszystkich kluczy w bazie. Wynikiem operacji jest komunikat
    składający się z pary <klucz>:<wartość>
get-min : znalezienie najmniejszej wartości przypisanej do wszystkich kluczy w bazie. Wynikiem operacji jest komunikat
    składający się z pary <klucz>:<wartość>.
new-record <klucz>:<wartość> : zapamiętanie nowej pary klucz:wartość w miejsce pary przechowywanej na węźle,
    do którego dany klient jest podłączony. Wynikiem tej operacji jest komunikat OK
terminate : powoduje odłączenie się węzła od sieci poprzez poinformowanie o tym fakcie swoich sąsiadów oraz zakończenie
    pracy. Sąsiedzi węzła poinformowani o zakończeniu przez niego pracy uwzględniają ten fakt w swoich zasobach
    i przestają się z nim komunikować. Przed samym zakończeniem pracy węzeł odsyła do klienta komunikat OK

W przypadku zaistnienia potrzeby komunikacji z innymi węzłami zostaje utworzony nowy socket tcp dla tego polecenia.
Polecenie zostaje rozesłane do wszystkich węzłów z którymi dany węzeł jest połączony, po czym oczekuje na odpowiedź.

Ponadto istnieją polecenia do komunikacji stricte pomiędzy węzłami:
terminating-server : węzeł otrzymuje informację o wyłączeniu węzła podanego w argumencie i usuwa go ze swojej hashmapy
add-to-map : węzeł otrzymuje informację o połączeniu się z nim innego węzła. Dodaje ten węzeł do hashmapy



