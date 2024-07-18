import { Button } from "@/components/button"
import { Input } from "@/components/input"
import { Modal } from "@/components/modal"
import { linksServer } from "@/server/link-server"
import { colors } from "@/styles/colors"
import { validateInput } from "@/utils/validateInput"
import { Plus } from "lucide-react-native"
import { useEffect, useState } from "react"
import { View, Text, Alert, FlatList } from "react-native"
import { TripLink, TripLinkProps } from "../../components/trip-link"
import { participantsServer } from "@/server/participant-server"
import { Participant, ParticipantProps } from "@/components/participant"

type DetailsProps = {
  tripId: string
}

enum StepModal {
  NONE = 0,
  NEW_LINK = 1
}

// por não ser defaul, o expo router nao reconhece como um rota e sim como um componente
export function Details({ tripId }: DetailsProps) {
  const [showModal, setShowModal] = useState(StepModal.NONE)
  const [linkUrl, setLinkUrl] = useState("")
  const [linkName, setLinkName] = useState("")
  const [links, setLinks] = useState<TripLinkProps[]>([])
  const [participants, setParticipants] = useState<ParticipantProps[]>([])
  const [isCreatingLink, setIsCreatingLink] = useState(false)

  function resetNewLinkFields() {
    setLinkName("")
    setLinkUrl("")
    setShowModal(StepModal.NONE)
  }

  async function handleCreateTripLink() {
    try {
      if (!linkName) {
        return Alert.alert("Link", "Preencha o título do link!")
      }

      if (!linkUrl.startsWith("http:") || !linkUrl.startsWith("https:")) {
        setLinkUrl("http:" + linkUrl)
      }

      if (!validateInput.url("http:" + linkUrl.trim())) {
        return Alert.alert("Link", "Link Inválido!")
      }

      setIsCreatingLink(true)

      await linksServer.create({
        tripId,
        title: linkName,
        url: linkUrl
      })

      Alert.alert("Link", "Link criado com sucesso!")

      resetNewLinkFields()
      await getTripLinks()
    } catch (error) {
      console.error(error)
    } finally {
      setIsCreatingLink(false)
    }
  }

  async function getTripParticipants() {
    try {
      const participants = await participantsServer.getByTripId(tripId)
      setParticipants(participants)
    } catch (error) {
      console.error(error)
    }
  }

  async function getTripLinks() {
    try {
      const links = await linksServer.getLinksByTripId(tripId)

      setLinks(links)
    } catch (error) {
      console.error(error)
    }
  }

  useEffect(() => {
    getTripLinks()
    getTripParticipants()
  }, [])

  return (
    <View className="flex-1 mt-10">
      <Text className="text-zinc-50 text-2xl font-semibold mb-2">Links Importantes</Text>

      <View className="flex-1">
        {links.length > 0 ? (
          <FlatList
            data={links}
            keyExtractor={item => item.id}
            renderItem={({ item }) => <TripLink data={item} />}
            contentContainerClassName="gap-4"
          />
        ) : (
          <Text className="text-zinc-400 font-regular text-base mt-2 mb-6">
            Nenhum link adicionado.
          </Text>
        )}

        <Button
          variant="secondary"
          onPress={() => setShowModal(StepModal.NEW_LINK)}
        >
          <Plus
            color={colors.zinc[200]}
            size={20}
          />
          <Button.Title>Cadastrar Novo Link</Button.Title>
        </Button>
      </View>

      <View className="flex-1 border-t border-zinc-800 my-6">
        <Text className="text-zinc-50 text-2xl font-semibold mb-2">Convidados</Text>

        <FlatList
          data={participants}
          keyExtractor={item => item.id}
          renderItem={({ item }) => <Participant data={item} />}
          contentContainerClassName="gap-4 pb-44"
        />
      </View>

      <Modal
        title="Cadastrar Link"
        subtitle="Todos os convidados podem visualizar os links importantes."
        visible={showModal === StepModal.NEW_LINK}
        onClose={() => setShowModal(StepModal.NONE)}
      >
        <View className="gap-2 mb-3">
          <Input variant="secondary">
            <Input.Field
              placeholder="Título do link"
              onChangeText={setLinkName}
              value={linkName}
            />
          </Input>

          <Input variant="secondary">
            <Input.Field
              placeholder="Url"
              onChangeText={setLinkUrl}
              value={linkUrl}
            />
          </Input>
        </View>

        <Button
          isLoading={isCreatingLink}
          onPress={handleCreateTripLink}
        >
          <Button.Title>Cadastrar Link</Button.Title>
        </Button>
      </Modal>
    </View>
  )
}
