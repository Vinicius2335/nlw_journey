import { Link2, Plus } from "lucide-react"
import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import { Button } from "../../components/button"
import { api } from "../../lib/axios"
import { Link, LinkListResponse } from "../../model/Link"
import { CreateLinkModal } from "./create-link-modal"

/**
 * Componente de visualização e adição de links importantes para a viagem
 */
export function ImportantLinks() {
  const { tripId } = useParams()
  const [links, setLinks] = useState<Link[]>([])
  const [isLinkModalOpen, setIsLinkModalOpen] = useState(false)

  function closeLinkModal() {
    setIsLinkModalOpen(false)
  }

  function openLinkModal() {
    setIsLinkModalOpen(true)
  }

  useEffect(() => {
    api.get<LinkListResponse>(`/trips/${tripId}/links`).then(resp => setLinks(resp.data.links))
  }, [tripId])

  return (
    <div className="space-y-6">
      <h2 className="font-semibold text-xl">Links Importantes</h2>

      <div className="space-y-5">
        {links.length > 0 ? (
          links.map(link => {
            return (
              <div
                key={link.id}
                className="flex items-center justify-between gap-4">
                <div className="space-y-1.5">
                  <span className="block font-medium text-zinc-100">{link.title}</span>
                  <a
                    href={"https://" + link.url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="block text-xs text-zinc-400 truncate hover:text-zinc-200 transition-colors">
                    {link.url}
                  </a>
                </div>
                <Link2 className="size-5 text-zinc-400" />
              </div>
            )
          })
        ) : (
          <p className="text-zinc-500 text-sm">Nenhum link cadastrado para essa viagem.</p>
        )}
      </div>

      <Button
        variant="secondary"
        onClick={openLinkModal}
        size="full">
        <Plus className="size-5" />
        Cadastrar Novo Link
      </Button>

      {isLinkModalOpen && <CreateLinkModal onCloseLinkModal={closeLinkModal} />}
    </div>
  )
}
