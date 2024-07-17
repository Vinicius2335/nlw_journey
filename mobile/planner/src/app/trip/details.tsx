import { View, Text } from "react-native"

type DetailsProps = {
  tripId: string
}

// por não ser defaul, o expo router nao reconhece como um rota e sim como um componente
export function Details({ tripId }: DetailsProps) {
  return (
    <View className="flex-1">
      <Text className="text-white">Details</Text>
    </View>
  )
}
