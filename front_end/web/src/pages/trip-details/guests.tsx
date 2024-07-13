import { CheckCircle2, CircleDashed } from "lucide-react"
import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import { api } from "../../lib/axios"
import { Participant, ParticipantListResponse } from "../../model/Participant"

/**
 * Componente de visualização dos convidados para a viagem, mostrando se eles confirmaram ou não
 */
export function Guests() {
  const { tripId } = useParams()
  const [participants, setParticipants] = useState<Participant[]>([])

  useEffect(() => {
    api
      .get<ParticipantListResponse>(`/trips/${tripId}/participants`)
      .then(resp => setParticipants(resp.data.participants))

  }, [tripId])

  return (
    <div className="space-y-5">
      {participants.map((participant, index) => {
        return (
          <div
            key={participant.id}
            className="flex items-center justify-between gap-4">
            <div className="space-y-1.5">
              <span className="block font-medium text-zinc-100">
                {participant.name === "" ? `Convidado ${index + 1}` : participant.name}
                </span>
              <span className="block text-sm text-zinc-400 truncate">{participant.email}</span>
            </div>
            {participant.is_confirmed ? (
              <CheckCircle2 className="size-5 text-lime-300 shrink-0" />
            ) : (
              <CircleDashed className="size-5 text-zinc-400 shrink-0" />
            )}
          </div>
        )
      })}
    </div>
  )
}
