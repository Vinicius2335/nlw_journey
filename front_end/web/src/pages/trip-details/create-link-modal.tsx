import { Link2, Tag } from "lucide-react"
import { FormEvent } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { CustomInput } from "../../components/custom-input"
import { CustomModal } from "../../components/custom-modal"
import { api } from "../../lib/axios"

interface CreateLinkModalProps {
  onCloseLinkModal: () => void
}

/**
 * Modal responsável por criar um link
 */
export function CreateLinkModal({ onCloseLinkModal }: CreateLinkModalProps) {
  const { tripId } = useParams()
  const navigate = useNavigate()

  async function createLink(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const data = new FormData(event.currentTarget)
    const title = data.get("title")?.toString()
    const url = data.get("url")?.toString()

    await api.post(`/trips/${tripId}/links`, { title, url })

    navigate(0)
  }

  return (
    <CustomModal
      onClickButton={createLink}
      onCloseModal={onCloseLinkModal}
      title={"Cadastrar Link"}
      describe={"Todos convidados podem visualizar os links importantes."}
      btnDescribe={"Salvar link"}>
      <CustomInput
        icon={Tag}
        placeholder={"Título do link"}
        name={"title"}
      />
      <CustomInput
        icon={Link2}
        placeholder={"URL"}
        name={"url"}
      />
    </CustomModal>
  )
}
