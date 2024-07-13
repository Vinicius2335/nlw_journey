import { Link2, Tag, X } from "lucide-react"
import { FormEvent } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { api } from "../../lib/axios"
import { Button } from "../../components/button"

interface CreateLinkModalProps {
  onCloseLinkModal: () => void
}

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
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[540px] rounded-xl py-5 px-6 shadow-shape bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex item-center justify-between">
            <h2 className="text-lg font-semibold">Cadastrar Link</h2>
            <button onClick={onCloseLinkModal}>
              <X className="side-5 text-zinc-400" />
            </button>
          </div>

          <p className="text-sm text-zinc-400">
            Todos convidados podem visualizar os links importantes.
          </p>
        </div>

        <form
          onSubmit={createLink}
          className="space-y-3">
          <div className="h-14 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
            <Tag className="text-zinc-400 size-5" />
            <input
              name="title"
              className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1"
              placeholder="Título do link"
            />
          </div>

          <div className="flex items-center gap-2">
            <div className="h-14 flex-1 px-4 bg-zinc-950 border border-zinc-800 rounded-lg flex items-center gap-2">
              <Link2 className="text-zinc-400 size-5" />
              <input
                name="url"
                className="bg-transparent text-lg placeholder-zinc-400 w-40 outline-none flex-1 [color-scheme:dark]"
                placeholder="URL"
              />
            </div>
          </div>

          <Button
            type="submit"
            size="full">
            Salvar link
          </Button>
        </form>
      </div>
    </div>
  )
}
