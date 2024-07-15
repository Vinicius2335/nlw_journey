import { Mail } from "lucide-react"
import { FormEvent } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { CustomInput } from "../../components/custom-input"
import { CustomModal } from "../../components/custom-modal"
import { api } from "../../lib/axios"

interface InviteGuestModalProps {
  onCloseInviteGuestModal: () => void
}

/**
 * Modal responsável por convidar um participante
 */
export function InviteGuestModal({ onCloseInviteGuestModal }: InviteGuestModalProps) {
  const { tripId } = useParams()
  const navigate = useNavigate()

  async function inviteNewGuest (event : FormEvent<HTMLFormElement>){
    event.preventDefault()

    const data = new FormData(event.currentTarget)
    const email = data.get('email')?.toString()
    const name = ""

    await api.post(`/trips/${tripId}/invite`, {name, email})

    navigate(0)
  }

  return (
    <CustomModal
      onCloseModal={onCloseInviteGuestModal}
      onClickButton={inviteNewGuest}
      title={"Convidar Participante"}
      describe={"Todos convidados podem visualizar os participantes."}
      btnDescribe={"Convidar"}>
      <CustomInput
        icon={Mail}
        type="email"
        placeholder={"Digite o e-mail do convidado"}
        name={"email"}
      />
    </CustomModal>
  )
}
