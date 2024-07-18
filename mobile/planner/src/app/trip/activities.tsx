import { Button } from "@/components/button"
import { Calendar } from "@/components/button-calendar"
import { Input } from "@/components/input"
import { Modal } from "@/components/modal"
import { activitiesServer } from "@/server/activity-server"
import { colors } from "@/styles/colors"
import dayjs from "dayjs"
import { Calendar as CalendarIcon, Clock, PlusIcon, Tag } from "lucide-react-native"
import { useEffect, useState } from "react"
import { Alert, Keyboard, SectionList, Text, View } from "react-native"
import { TripData } from "./[id]"
import { Activity, ActivityProps } from "@/components/activity"
import { Loading } from "@/components/loading"

enum StepModal {
  NONE = 0,
  CALENDAR = 1,
  NEW_ACTIVITY = 2
}

type ActivitiesProps = {
  tripDetails: TripData
}

type TripActivities = {
  title: {
    dayNumber: number
    dayName: String
  }
  data: ActivityProps[]
}

// por não ser defaul, o expo router nao reconhece como um rota e sim como um componente
export function Activities({ tripDetails }: ActivitiesProps) {
  const [showModal, setShowModal] = useState(StepModal.NONE)

  // Datas
  const [activityTitle, setActivityTitle] = useState("")
  const [activityDate, setActivityDate] = useState("")
  const [activityHour, setActivityHour] = useState("")
  const [tripActivities, setTripActivities] = useState<TripActivities[]>([])

  // Loadings
  const [isCreatingActivity, setIsCreatingActivity] = useState(false)
  const [isLoadingActivities, setIsLoadingActivities] = useState(true)

  function resetNewActivityFields() {
    setActivityDate("")
    setActivityTitle("")
    setActivityHour("")
    setShowModal(StepModal.NONE)
  }

  async function handleCreateActivity() {
    try {
      if (!activityTitle || !activityDate || !activityHour) {
        return Alert.alert("Cadastrar Atividade", "Preencha todos os campos!")
      }

      setIsCreatingActivity(true)

      await activitiesServer.create({
        tripId: tripDetails.id,
        occurs_at: dayjs(activityDate).add(Number(activityHour), "h").toISOString(),
        title: activityTitle
      })

      Alert.alert("Nova Atividade", "Nova atividade cadastrada com sucesso!")

      await getTripActivities()
      resetNewActivityFields()
    } catch (error) {
      console.error(error)
    } finally {
      setIsCreatingActivity(false)
    }
  }

  async function getTripActivities() {
    try {
      const activities = await activitiesServer.getActivitiesByTripId(tripDetails.id)

      const activitiesToSectionList = activities.map(dayActivity => ({
        title: {
          dayNumber: dayjs(dayActivity.date).date(),
          dayName: dayjs(dayActivity.date).format("dddd").replace("-feira", "")
        },
        data: dayActivity.activities.map(activity => ({
          id: activity.id,
          title: activity.title,
          hour: dayjs(activity.occurs_at).format("HH[:]mm[h]"),
          isBefore: dayjs(activity.occurs_at).isBefore(dayjs())
        }))
      }))

      setTripActivities(activitiesToSectionList)
    } catch (error) {
      console.error(error)
    } finally {
      setIsLoadingActivities(false)
    }
  }

  useEffect(() => {
    getTripActivities()
  }, [])

  return (
    <View className="flex-1">
      <View className="w-full flex-row mt-5 mb-6 items-center">
        <Text className="text-zinc-50 text-2xl font-semibold flex-1">Atividades</Text>

        <Button onPress={() => setShowModal(StepModal.NEW_ACTIVITY)}>
          <PlusIcon
            color={colors.lime[950]}
            size={20}
          />
          <Button.Title>Nova Atividade</Button.Title>
        </Button>
      </View>

      {isLoadingActivities ? (
        <Loading />
      ) : (
        <SectionList
          sections={tripActivities}
          keyExtractor={item => item.id}
          renderItem={({ item }) => <Activity data={item} />}
          renderSectionHeader={({ section }) => (
            <View className="w-full">
              <Text className="text-zinc-50 text-2xl font-semibold py-2">
                Dia {section.title.dayNumber + " "}
                <Text className="text-zinc-500 text-base font-regular capitalize">
                  {section.title.dayName}
                </Text>
              </Text>

              {section.data.length === 0 && (
                <Text className="text-zinc-500 font-regular text-sm mb-8">
                  Nenhuma atividade cadastrada nessa data.
                </Text>
              )}
            </View>
          )}
          contentContainerClassName="gap-3 pb-48"
          showsVerticalScrollIndicator={false}
        />
      )}

      {/* MODAL PARA CRIAR UMA NOVA ATIVIDADE */}
      <Modal
        title={"Cadastrar Atividade"}
        subtitle="Todos os convidados podem visualizar as atividades"
        visible={showModal === StepModal.NEW_ACTIVITY}
        onClose={() => setShowModal(StepModal.NONE)}
      >
        <View className="mt-4 mb-3 gap-2">
          <Input variant="secondary">
            <Tag
              color={colors.zinc[400]}
              size={20}
            />
            <Input.Field
              placeholder="Qual atividade?"
              onChangeText={setActivityTitle}
              value={activityTitle}
            />
          </Input>

          <View className="w-full mt-2 flex-row gap-2">
            <Input
              variant="secondary"
              className="flex-1"
            >
              <CalendarIcon
                color={colors.zinc[400]}
                size={20}
              />
              <Input.Field
                placeholder="Data?"
                value={activityDate ? dayjs(activityDate).format("DD [de] MMMM") : ""}
                onFocus={() => Keyboard.dismiss()}
                showSoftInputOnFocus={false}
                onPressIn={() => setShowModal(StepModal.CALENDAR)}
              />
            </Input>

            <Input
              variant="secondary"
              className="flex-1"
            >
              <Clock
                color={colors.zinc[400]}
                size={20}
              />
              <Input.Field
                placeholder="Horário?"
                onChangeText={text => setActivityHour(text.replace(".", "").replace(",", ""))}
                value={activityHour}
                keyboardType="numeric"
                maxLength={2}
              />
            </Input>
          </View>
        </View>

        <Button
          onPress={handleCreateActivity}
          isLoading={isCreatingActivity}
        >
          <Button.Title>Cadastrar Atividade</Button.Title>
        </Button>
      </Modal>

      {/* MODAL PARA SELECIONAR DATA DA ATIVIDADE*/}
      <Modal
        title="Selecionar data"
        subtitle="Selecione a data da atividade"
        visible={showModal === StepModal.CALENDAR}
        onClose={() => setShowModal(StepModal.NONE)}
      >
        <View className="gap-4 mt-4">
          <Calendar
            onDayPress={day => setActivityDate(day.dateString)}
            markedDates={{ [activityDate]: { selected: true } }}
            initialDate={tripDetails.starts_at.toString()}
            minDate={tripDetails.starts_at.toString()}
            maxDate={tripDetails.ends_at.toString()}
          />
        </View>
        <Button onPress={() => setShowModal(StepModal.NEW_ACTIVITY)}>
          <Button.Title>Confirmar</Button.Title>
        </Button>
      </Modal>
    </View>
  )
}
