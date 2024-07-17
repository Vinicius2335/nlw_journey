import { View, Text } from "react-native";
import { TripData } from "./[id]"

type ActivitiesProps = {
  tripDetails: TripData
}

// por não ser defaul, o expo router nao reconhece como um rota e sim como um componente
export function Activities ({ tripDetails }: ActivitiesProps){
  return (
    <View className="flex-1">
        <Text className="text-white">
            Activities
        </Text>
    </View>
  )
}