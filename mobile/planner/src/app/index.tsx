import { Button } from "@/components/button"
import { Calendar } from "@/components/button-calendar"
import { GuestEmail } from "@/components/guest-email-button"
import { Input } from "@/components/input"
import { Loading } from "@/components/loading"
import { Modal } from "@/components/modal"
import { tripServer } from "@/server/trip-server"
import { tripStorage } from "@/storage/trip"
import { colors } from "@/styles/colors"
import { calendarUtils, DatesSelected } from "@/utils/calendarUtils"
import { validateInput } from "@/utils/validateInput"
import dayjs from "dayjs"
import { router } from "expo-router"
import {
  ArrowRight,
  AtSign,
  Calendar as IconCalendar,
  MapPin,
  Settings2,
  UserRoundPlus
} from "lucide-react-native"
import { useEffect, useState } from "react"
import { Alert, Image, Keyboard, Text, View } from "react-native"
import { DateData } from "react-native-calendars"

enum StepForm {
  TRIP_DETAILS = 1,
  ADD_EMAIL = 2
}

enum StepModal {
  NONE = 0,
  CALENDAR = 1,
  GUESTS = 2
}

// Para o expo Router reconhecer um elemento deve estar dentro da pasta app e do default
export default function Index() {
  const [stepForm, setStepForm] = useState(StepForm.TRIP_DETAILS)
  const [showModal, setShowModal] = useState(StepModal.NONE)
  const [selectedDates, setSelectedDates] = useState({} as DatesSelected)
  const [destination, setDestination] = useState("")
  const [emailToInvite, setEmailToInvite] = useState("")
  const [emailsToInvite, setEmailsToInvite] = useState<string[]>([])
  const [isCreatingTrip, setIsCreatingTrip] = useState(false)
  const [isGettingTrip, setIsGettingTrip] = useState(true)

  // handle quando a função é disparada por uma interação do usuário
  function handleNextStepForm() {
    if (destination.trim().length === 0 || !selectedDates.startsAt || !selectedDates.endsAt) {
      return Alert.alert(
        "Detalhes da Viagem",
        "Preencha todas as informaçoes da viagem para seguir."
      )
    }

    if (destination.length < 4) {
      return Alert.alert("Detalhes da Viagem", "O destino deve ter pelo menos 4 caracteres.")
    }

    if (stepForm === StepForm.TRIP_DETAILS) {
      return setStepForm(StepForm.ADD_EMAIL)
    }

    Alert.alert("Nova Viagem", "Confirmar Viagem?", [
      {
        text: "Não",
        style: "cancel"
      },
      {
        text: "Sim",
        onPress: createTrip
      }
    ])
  }

  function handleSelectDate(selectedDay: DateData) {
    const dates = calendarUtils.orderStartsAtAndEndsAt({
      startsAt: selectedDates.startsAt,
      endsAt: selectedDates.endsAt,
      selectedDay
    })

    setSelectedDates(dates)
  }

  function handleRemoveEmail(emailToRemove: string) {
    setEmailsToInvite(prevState => prevState.filter(email => email !== emailToRemove))
  }

  function handleAddEmail() {
    if (!validateInput.email(emailToInvite)) {
      return Alert.alert("Convidado", "E-mail inválido!")
    }

    const emailAlreadyExists = emailsToInvite.find(email => email === emailToInvite)

    if (emailAlreadyExists) {
      return Alert.alert("Convidado", "E-mail já foi adicionado!")
    }

    setEmailsToInvite([...emailsToInvite, emailToInvite])
    setEmailToInvite("")
  }

  async function saveTrip(tripId: string) {
    try {
      await tripStorage.save(tripId)
      router.navigate("/trip/" + tripId)
    } catch (error) {
      Alert.alert("Salvar Viagem", "Não foi possível salvar o id da viagem no dispositivo!")

      console.error(error)
    }
  }

  async function createTrip() {
    try {
      setIsCreatingTrip(true)

      const newTrip = await tripServer.create({
        destination,
        starts_at: dayjs(selectedDates.startsAt?.dateString).toISOString(),
        ends_at: dayjs(selectedDates.endsAt?.dateString).toISOString(),
        emails_to_invite: emailsToInvite
      })

      Alert.alert("Nova Viagem", "Viagem criada com sucesso!", [
        {
          text: "OK. Continuar.",
          onPress: () => saveTrip(newTrip.trip_id)
        }
      ])
    } catch (error) {
      console.error(Error)
      setIsCreatingTrip(false)
    }
  }

  async function getTrip() {
    try {
      const tripId = await tripStorage.get()

      if (!tripId) {
        return setIsGettingTrip(false)
      }

      const trip = await tripServer.getById(tripId)

      if (trip) {
        return router.navigate("/trip/" + trip.id)
      }
    } catch (error) {
      setIsGettingTrip(false)
      console.error(error)
    }
  }

  // Toda vez que a tela for renderizada
  // é verificado se existe um tripId armazenado localmente no dispositivo
  // se não, não faz nada
  // se encontrado, redireciona o usuário para a tela de trip-details([id].tsx)
  useEffect(() => {
    getTrip()
  }, [])

  if (isGettingTrip) {
    return <Loading />
  }

  return (
    <View className="flex-1 justify-center items-center px-5 ">
      <Image
        source={require("@/assets/logo.png")}
        className="h-8"
        resizeMode="contain"
      />

      <Image
        source={require("@/assets/bg.png")}
        className="absolute"
      />

      <Text className="text-zinc-400 font-regular text-center text-lg mt-3">
        Convide seus amigos e planeje sua {"\n"} próxima viagem
      </Text>

      <View className="w-full bg-zinc-900 p-4 rounded-lg my-8 border border-zinc-800">
        {/* ADICIONA DESTINO */}
        <Input>
          <MapPin
            color={colors.zinc[400]}
            size={20}
          />
          <Input.Field
            placeholder="Para onde?"
            editable={stepForm === StepForm.TRIP_DETAILS}
            value={destination}
            onChangeText={setDestination}
          />
        </Input>

        {/* ADICIONA DATA IDA E VOLTA */}
        <Input>
          <IconCalendar
            color={colors.zinc[400]}
            size={20}
          />
          <Input.Field
            placeholder="Quando?"
            onFocus={() => Keyboard.dismiss()} // remove o teclado quando é focado
            editable={stepForm === StepForm.TRIP_DETAILS}
            showSoftInputOnFocus={false}
            onPressIn={() => stepForm === StepForm.TRIP_DETAILS && setShowModal(StepModal.CALENDAR)}
            value={selectedDates.formatDatesInText}
          />
        </Input>

        {stepForm === StepForm.ADD_EMAIL && (
          <>
            <View className="border-b py-3 border-zinc-800">
              <Button
                variant="secondary"
                onPress={() => setStepForm(StepForm.TRIP_DETAILS)}
              >
                <Button.Title>Alterar Local/Data </Button.Title>
                <Settings2
                  color={colors.zinc[200]}
                  size={20}
                />
              </Button>
            </View>
            <Input>
              <UserRoundPlus
                color={colors.zinc[400]}
                size={20}
              />
              <Input.Field
                placeholder="Quem estará na viagem?"
                autoCorrect={false}
                value={
                  emailsToInvite.length > 0 ? `${emailsToInvite.length} pessoa(s) convidado(s)` : ""
                }
                onPress={() => {
                  Keyboard.dismiss()
                  setShowModal(StepModal.GUESTS)
                }}
                showSoftInputOnFocus={false}
              />
            </Input>
          </>
        )}

        <Button
          onPress={handleNextStepForm}
          isLoading={isCreatingTrip}
        >
          <Button.Title>
            {stepForm === StepForm.TRIP_DETAILS ? "Continuar" : "Confirmar Viagem"}
          </Button.Title>
          <ArrowRight
            color={colors.lime[950]}
            size={20}
          />
        </Button>
      </View>

      <Text className="text-zinc-500 font-regular text-center text-base">
        Ao planejar a sua viagem pela plann.er você automaticamente concorda com nossos{" "}
        <Text className="text-zinc-300 underline"> termos de uso e políticas de privacidade.</Text>
      </Text>

      {/* MODAL PARA SELECIONAR DATAS */}
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
        <Button onPress={() => setShowModal(StepModal.NONE)}>
          <Button.Title>Confirmar</Button.Title>
        </Button>
      </Modal>

      {/* MODAL PARA ADICIONAR CONVIDADOS */}
      <Modal
        title="Selecionar Convidados"
        subtitle="Os convidados irão receber e-mails para confirmar a participação na viagem"
        visible={showModal === StepModal.GUESTS}
        onClose={() => setShowModal(StepModal.NONE)}
      >
        <View className="my-2 flex-wrap border-b border-zinc-800 py-5 items-start">
          {emailsToInvite.length > 0 ? (
            emailsToInvite.map(email => (
              <GuestEmail
                key={email}
                email={email}
                onRemove={() => handleRemoveEmail(email)}
              />
            ))
          ) : (
            <Text className="text-zinc-400 mb-2 text-base font-regular">
              Nenhum e-mail adicionado.
            </Text>
          )}
        </View>

        {/* ADICIONA PARTICIPANTE */}
        <View className="gap-4 mt-4">
          <Input variant="secondary">
            <AtSign
              color={colors.zinc[400]}
              size={20}
            />
            <Input.Field
              placeholder="Digite o e-mail do convidado"
              keyboardType="email-address"
              onChangeText={text => setEmailToInvite(text.toLowerCase())}
              value={emailToInvite}
              returnKeyType="send" // altera o icone do botao de enter do teclado
              onSubmitEditing={handleAddEmail}
            />
          </Input>

          <Button onPress={handleAddEmail}>
            <Button.Title>Convidar</Button.Title>
          </Button>
        </View>
      </Modal>
    </View>
  )
}
