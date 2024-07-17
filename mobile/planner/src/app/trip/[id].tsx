import { Button } from "@/components/button"
import { Calendar } from "@/components/button-calendar"
import { Input } from "@/components/input"
import { Loading } from "@/components/loading"
import { Modal } from "@/components/modal"
import { TripDetails, tripServer } from "@/server/trip-server"
import { colors } from "@/styles/colors"
import { calendarUtils, DatesSelected } from "@/utils/calendarUtils"
import dayjs from "dayjs"
import { router, useLocalSearchParams } from "expo-router"
import {
  CalendarRange,
  Calendar as IconCalendar,
  Info,
  MapPin,
  Settings2
} from "lucide-react-native"
import { useEffect, useState } from "react"
import { Alert, Keyboard, TouchableOpacity, View } from "react-native"
import { DateData } from "react-native-calendars"
import { Activities } from "./activities"
import { Details } from "./details"

export type TripData = TripDetails & {
  when: string
}

enum StepModal {
  NONE = 0,
  UPDATE_TRIP = 1,
  CALENDAR = 2
}

export default function Trip() {
  const tripId = useLocalSearchParams<{ id: string }>().id
  const [isLoadingTrip, setIsLoadingTrip] = useState(true)
  const [isUpdatingTrip, setIsUpdatingTrip] = useState(false)
  const [showModal, setShowModal] = useState(StepModal.NONE)
  const [tripDetails, setTripDetails] = useState({} as TripData)
  const [option, setOption] = useState<"activity" | "details">("activity")
  const [destination, setDestination] = useState("")
  const [selectedDates, setSelectedDates] = useState({} as DatesSelected)

  async function getTripDetails() {
    try {
      setIsLoadingTrip(true)

      if (!tripId) {
        return router.back()
      }

      const trip = await tripServer.getById(tripId)

      const maxLengthDestination = 14
      const destination =
        trip.destination.length > maxLengthDestination
          ? trip.destination.slice(0, maxLengthDestination) + "..."
          : trip.destination

      const starts_at = dayjs(trip.starts_at).format("DD")
      const ends_at = dayjs(trip.ends_at).format("DD")
      const month = dayjs(trip.starts_at).format("MMM")

      setTripDetails({
        ...trip,
        when: `${destination} de ${starts_at} à ${ends_at} de ${month}.`
      })
      setDestination(trip.destination)
    } catch (error) {
      console.error(error)
    } finally {
      setIsLoadingTrip(false)
    }
  }

  function handleSelectDate(selectedDay: DateData) {
    const dates = calendarUtils.orderStartsAtAndEndsAt({
      startsAt: selectedDates.startsAt,
      endsAt: selectedDates.endsAt,
      selectedDay
    })

    setSelectedDates(dates)
  }

  async function handleUpdateTrip() {
    try {
      if (!tripId) {
        return
      }

      if (!destination || !selectedDates.startsAt || !selectedDates.endsAt) {
        return Alert.alert(
          "Atualizar Viagem",
          "Lembre-se de, além de preencher o destino, selecione data de início e fim da viagem."
        )
      }

      setIsUpdatingTrip(true)

      await tripServer.update({
        id: tripId,
        destination,
        starts_at: dayjs(selectedDates.startsAt?.dateString).toISOString(),
        ends_at: dayjs(selectedDates.endsAt?.dateString).toISOString()
      })

      Alert.alert(
        "Atualizar Viagem",
        "Viagem atualizada com sucesso!",
        [
          {
            text: "OK",
            onPress: () => {
              setShowModal(StepModal.NONE)
              getTripDetails()
            }
          }
        ]
      )
    } catch (error) {
      console.error(error)
    } finally {
      setIsUpdatingTrip(false)
    }
  }

  useEffect(() => {
    getTripDetails()
  }, [])

  if (isLoadingTrip) {
    return <Loading />
  }

  return (
    <View className="flex-1 px-5 pt-16">
      {/* HEADER */}
      <Input variant="tertiary">
        <MapPin
          color={colors.zinc[400]}
          size={20}
        />
        <Input.Field
          value={tripDetails.when}
          readOnly
        />

        <TouchableOpacity
          activeOpacity={0.6}
          onPress={() => setShowModal(StepModal.UPDATE_TRIP)}
          className="w-9 h-9 bg-zinc-800 items-center justify-center rounded">
          <Settings2
            color={colors.zinc[400]}
            size={20}
          />
        </TouchableOpacity>
      </Input>

      {option === "activity" ? (
        <Activities tripDetails={tripDetails} />
      ) : (
        <Details tripId={tripDetails.id} />
      )}

      {/* MENU */}
      <View className="w-full absolute -bottom-1 self-center justify-end pb-5 z-10 bg-zinc-950">
        <View className="w-full flex-row bg-zinc-900 p-4 rounded-lg border border-zinc-800 gap-2">
          <Button
            className="flex-1"
            onPress={() => setOption("activity")}
            variant={option === "activity" ? "primary" : "secondary"}>
            <CalendarRange
              color={option === "activity" ? colors.lime[950] : colors.zinc[200]}
              size={20}
            />
            <Button.Title>Atividades</Button.Title>
          </Button>

          <Button
            className="flex-1"
            onPress={() => setOption("details")}
            variant={option === "details" ? "primary" : "secondary"}>
            <Info
              color={option === "details" ? colors.lime[950] : colors.zinc[200]}
              size={20}
            />
            <Button.Title>Detalhes</Button.Title>
          </Button>
        </View>
      </View>

      {/* MODAL ATUALIZAR VIAGEM */}
      <Modal
        title="Atualizar Viagem"
        subtitle="Somente quem criou a viagem pode editar."
        visible={showModal === StepModal.UPDATE_TRIP}
        onClose={() => setShowModal(StepModal.NONE)}>
        <View className="gap-2 my-4">
          <Input variant="secondary">
            <MapPin
              color={colors.zinc[400]}
              size={20}
            />
            <Input.Field
              placeholder="Para onde?"
              onChangeText={setDestination}
              value={destination}
            />
          </Input>

          <Input variant="secondary">
            <IconCalendar
              color={colors.zinc[400]}
              size={20}
            />
            <Input.Field
              placeholder="Quando?"
              value={selectedDates.formatDatesInText}
              onPressIn={() => setShowModal(StepModal.CALENDAR)}
              onFocus={() => Keyboard.dismiss()}
            />
          </Input>

          <Button
            onPress={handleUpdateTrip}
            isLoading={isUpdatingTrip}>
            <Button.Title>Atualizar</Button.Title>
          </Button>
        </View>
      </Modal>

      {/* MODAL CALENDARIO */}
      <Modal
        title="Selecionar datas"
        subtitle="Selecione a data de ida e volta da viagem"
        visible={showModal === StepModal.CALENDAR}
        onClose={() => setShowModal(StepModal.NONE)}>
        <View className="gap-4 mt-4">
          <Calendar
            onDayPress={handleSelectDate}
            markedDates={selectedDates.dates}
            minDate={dayjs().toISOString()}
          />
        </View>
        <Button onPress={() => setShowModal(StepModal.UPDATE_TRIP)}>
          <Button.Title>Confirmar</Button.Title>
        </Button>
      </Modal>
    </View>
  )
}
