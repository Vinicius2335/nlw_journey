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
  Mail,
  MapPin,
  Settings2,
  User
} from "lucide-react-native"
import { useEffect, useState } from "react"
import { Alert, Keyboard, Text, TouchableOpacity, View } from "react-native"
import { DateData } from "react-native-calendars"
import { Activities } from "./activities"
import { Details } from "./details"
import { validateInput } from "@/utils/validateInput"
import { participantsServer } from "@/server/participant-server"
import { tripStorage } from "@/storage/trip"

export type TripData = TripDetails & {
  when: string
}

enum StepModal {
  NONE = 0,
  UPDATE_TRIP = 1,
  CALENDAR = 2,
  CONFIRM_ATTENDANCE = 3
}

export default function Trip() {
  const tripParams = useLocalSearchParams<{ id: string; participant?: string }>()

  const [isLoadingTrip, setIsLoadingTrip] = useState(true)
  const [isUpdatingTrip, setIsUpdatingTrip] = useState(false)
  const [isConfirmingAttendance, setIsConfirmingAttendance] = useState(false)

  const [showModal, setShowModal] = useState(StepModal.NONE)

  const [tripDetails, setTripDetails] = useState({} as TripData)
  const [option, setOption] = useState<"activity" | "details">("activity")
  const [selectedDates, setSelectedDates] = useState({} as DatesSelected)
  const [destination, setDestination] = useState("")
  const [guestName, setGuestName] = useState("")
  const [guestEmail, setGuestEmail] = useState("")

  async function getTripDetails() {
    try {
      setIsLoadingTrip(true)

      if (!tripParams.participant) {
        setShowModal(StepModal.CONFIRM_ATTENDANCE)
      }

      if (!tripParams.id) {
        return router.back()
      }

      const trip = await tripServer.getById(tripParams.id)

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
      if (!tripParams.id) {
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
        id: tripParams.id,
        destination,
        starts_at: dayjs(selectedDates.startsAt?.dateString).toISOString(),
        ends_at: dayjs(selectedDates.endsAt?.dateString).toISOString()
      })

      Alert.alert("Atualizar Viagem", "Viagem atualizada com sucesso!", [
        {
          text: "OK",
          onPress: () => {
            setShowModal(StepModal.NONE)
            getTripDetails()
          }
        }
      ])
    } catch (error) {
      console.error(error)
    } finally {
      setIsUpdatingTrip(false)
    }
  }

  async function handleConfirmAttendance() {
    try {
      if (!tripParams.participant || !tripParams.id) {
        return
      }

      if (!guestName.trim() || !guestEmail.trim()) {
        return Alert.alert("Confirmação", "Preencha todos os campos!")
      }

      if (!validateInput.email(guestEmail.trim())) {
        return Alert.alert("Confirmação", "Email inválido!")
      }

      setIsConfirmingAttendance(true)

      await participantsServer.confirmTripByParticipantId({
        participantId: tripParams.participant,
        email: guestEmail.trim(),
        name: guestName
      })

      Alert.alert("Confirmação", "Viagem confirmada com sucesso!")

      await tripStorage.save(tripParams.id)

      setShowModal(StepModal.NONE)
    } catch (error) {
      console.error(error)
      Alert.alert("Confirmação", "Não foi possivel confirmar!")
    } finally {
      setIsConfirmingAttendance(false)
    }
  }

  // Toda vez que a tela for renderizada
  // é verificado se existe um tripId armazenado localmente no dispositivo
  // se não, retorna para a tela inicial
  // se encontrado, busca os detalhes desse tripId
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
          className="w-9 h-9 bg-zinc-800 items-center justify-center rounded"
        >
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
            variant={option === "activity" ? "primary" : "secondary"}
          >
            <CalendarRange
              color={option === "activity" ? colors.lime[950] : colors.zinc[200]}
              size={20}
            />
            <Button.Title>Atividades</Button.Title>
          </Button>

          <Button
            className="flex-1"
            onPress={() => setOption("details")}
            variant={option === "details" ? "primary" : "secondary"}
          >
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
        onClose={() => setShowModal(StepModal.NONE)}
      >
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
            isLoading={isUpdatingTrip}
          >
            <Button.Title>Atualizar</Button.Title>
          </Button>
        </View>
      </Modal>

      {/* MODAL CALENDARIO */}
      <Modal
        title="Selecionar datas"
        subtitle="Selecione a data de ida e volta da viagem"
        visible={showModal === StepModal.CALENDAR}
        onClose={() => setShowModal(StepModal.NONE)}
      >
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

      {/* MODAL CONFIRMAR EMAIL */}
      <Modal
        title="Confirmar E-mail"
        visible={showModal === StepModal.CONFIRM_ATTENDANCE}
      >
        <View className="gap-4 mt-4">
          <Text className="text-zinc-400 font-regular leading-6 my-20">
            Você foi convidado(a) para uma viagem para{" "}
            <Text className="font-semibold text-zinc-100">{tripDetails.destination} </Text>
            nas datas de{" "}
            <Text className="font-semibold text-zinc-100">
              {dayjs(tripDetails.starts_at).date()} a {dayjs(tripDetails.ends_at).date()} de{" "}
              {dayjs(tripDetails.ends_at).format("MMMM")}. {"\n\n"}
              Para confirmar sua presença na viagem, preencha os dados abaixo:
            </Text>
            <Input variant="secondary">
              <User
                color={colors.zinc[400]}
                size={20}
              />
              <Input.Field
                placeholder="Seu nome completo"
                onChangeText={setGuestName}
                value={guestName}
              />
            </Input>
            <Input variant="secondary">
              <Mail
                color={colors.zinc[400]}
                size={20}
              />
              <Input.Field
                placeholder="Email de confirmação"
                keyboardType="email-address"
                onChangeText={setGuestEmail}
                value={guestEmail}
              />
            </Input>
            <Button
              isLoading={isConfirmingAttendance}
              onPress={handleConfirmAttendance}
            >
              <Button.Title>Confirmar Presença</Button.Title>
            </Button>
          </Text>
        </View>
      </Modal>
    </View>
  )
}
