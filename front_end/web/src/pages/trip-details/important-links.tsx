import { Link2, Plus } from "lucide-react"
import { Button } from "../../components/button"

// NOTE - FALTOU O HREF

/**
 * Componente de visualização e adição de links importantes para a viagem
 */
export function ImportantLinks() {
  return (
    <div className="space-y-6">
      <h2 className="font-semibold text-xl">Links Importantes</h2>

      <div className="space-y-5">
        <div className="flex items-center justify-between gap-4">
          <div className="space-y-1.5">
            <span className="block font-medium text-zinc-100">Reserva AirBnB</span>
            <a
              href="#"
              target="_blank"
              className="block text-xs text-zinc-400 truncate hover:text-zinc-200 transition-colors">
              www.google.com.br
            </a>
          </div>
          <Link2 className="size-5 text-zinc-400" />
        </div>

        <div className="flex items-center justify-between gap-4">
          <div className="space-y-1.5">
            <span className="block font-medium text-zinc-100">Nlw</span>
            <a
              href="https://nlw-journey.apidocumentation.com/reference"
              target="_blank"
              className="block text-xs text-zinc-400 truncate hover:text-zinc-200 transition-colors">
              https://nlw-journey.apidocumentation.com/reference
            </a>
          </div>
          <Link2 className="size-5 text-zinc-400" />
        </div>
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
