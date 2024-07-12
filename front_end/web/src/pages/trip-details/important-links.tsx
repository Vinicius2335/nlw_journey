import { Link2, Plus } from "lucide-react"
import { Button } from "../../components/button"
import { useParams } from "react-router-dom"
import { useEffect, useState } from "react"
import { api } from "../../lib/axios"
import { Link, LinkListResponse } from "../../model/LinkListResponse"

// NOTE - FALTOU O HREF

/**
 * Componente de visualização e adição de links importantes para a viagem
 */
export function ImportantLinks() {
  const { tripId } = useParams()
  const [links, setLinks] = useState<Link[]>([])

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
              <div key={link.id} className="flex items-center justify-between gap-4">
                <div className="space-y-1.5">
                  <span className="block font-medium text-zinc-100">{link.title}</span>
                  <a
                    href={link.url}
                    target="_blank"
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
        size="full">
        <Plus className="size-5" />
        Cadastrar Novo Link
      </Button>
    </div>
  )
}
