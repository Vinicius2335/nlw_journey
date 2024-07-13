import { Calendar, Tag } from "lucide-react"
import { FormEvent } from "react"
import { useParams } from "react-router-dom"
import { CustomInput } from "../../components/custom-input"
import { CustomModal } from "../../components/custom-modal"
import { api } from "../../lib/axios"

interface CreateActivityModalProps {
  onCloseActivityModal: () => void
}

/**
 * Modal responsável por criar uma atividade
 */
export function CreateActivityModal({ onCloseActivityModal }: CreateActivityModalProps) {
  const { tripId } = useParams()

  async function createActivity (event : FormEvent<HTMLFormElement>){
    event.preventDefault()

    const data = new FormData(event.currentTarget)
    const title = data.get('title')?.toString()
    const occurs_at = data.get('occursAt')?.toString()

    await api.post(`/trips/${tripId}/activities`, {title, occurs_at})

    window.document.location.reload()
  }

  return (
    <CustomModal
      onClickButton={createActivity}
      onCloseModal={onCloseActivityModal}
      title={"Cadastrar Atividade"}
      describe={"Todos convidados podem visualizar as atividades."}
      btnDescribe={"Salvar Atividade"}>
      <CustomInput
        icon={Tag}
        placeholder={"Qual a atividade?"}
        name={"title"}
      />
      <CustomInput
        type="datetime-local"
        icon={Calendar}
        placeholder={"Data e Horário da atividade"}
        name={"occursAt"}
      />
    </CustomModal>
  )
}
