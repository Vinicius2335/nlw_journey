## Dependencias Instaladas
- [Nativewind - css](https://www.nativewind.dev/v4/getting-started/expo-router)
- [Expo Fonts](https://docs.expo.dev/develop/user-interface/fonts/)
- [Clsx - aplica classes/variantes de forma condicional](https://www.npmjs.com/package/clsx)
- [Lucide - icons](https://lucide.dev/guide/packages/lucide-react-native)
  - + react-native-svg pq os icones estão em svg
- [Calendário - component](https://github.com/wix/react-native-calendars)
- [Expo Blur View](https://docs.expo.dev/versions/latest/sdk/blur-view/)
  - deixa o fundo embaçado
- [Axios - requisições](https://axios-http.com/ptbr/docs/intro)
- [Dayjs - formatar datas](https://github.com/iamkun/dayjs)
- [Zod - validações](https://zod.dev)
- [AsyncStorage](https://docs.expo.dev/versions/latest/sdk/async-storage/)

## Não esquecer
- como configurar as importaçoes em tsconfig.json
- ``npx expo start``
- arquivos com _ = arquivos de configuração das rotas de navegação
- ``npx expo start --clear``
- quebrar a linha ``{"\n"}``

## Exemplo Response
- getById 
````json
  {
    "confirmed": false,
    "destination": "Rio de Janeiro, RJ",
    "ends_at": "2024-07-30T03:00:00",
    "id": "e065a222-ea48-4caa5-bd54-2683364b9f0e",
    "owner_email": "owner@email.com",
    "owner_name": "Owner Name",
    "starts_at": "2024-07-818T03:00:00"
  }

````