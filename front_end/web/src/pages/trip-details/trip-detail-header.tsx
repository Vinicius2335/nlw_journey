import { MapPin, Calendar, Settings2 } from "lucide-react"
import { Button } from "../../components/button"
import { useParams } from "react-router-dom"
import { useEffect, useState } from "react"
import { api } from "../../lib/axios"
import { Trip } from "../../model/Trip"
import { format } from "date-fns"
import { UpdateTripModal } from "./update-trip-modal"

/**
 * Header contendo o destino e a data da viagem
 */
export function TripDetailHeader() {
  const { tripId } = useParams()
  const [trip, setTrip] = useState<Trip | undefined>()
  const [isUpdateTripModelOpen, setIsUpdateTripModelOpen] = useState(false)

  const displayedDate = trip
    ? format(trip.starts_at, "d ' de ' LLL")
        .concat(" até ")
        .concat(format(trip.ends_at, "d ' de ' LLL"))
    : null

    function openUpdateTripModal() {
      setIsUpdateTripModelOpen(true)
    }
  
    function closeUpdateTripModal() {
      setIsUpdateTripModelOpen(false)
    }

  useEffect(() => {
    api.get<Trip>(`/trips/${tripId}`).then(resp => setTrip(resp.data))
  }, [tripId])

  return (
    <div className="px-4 h-16 rounded-xl bg-zinc-900 shadow-shape flex items-center justify-between">
      <div className="flex items-center gap-2">
        <MapPin className="size-5 text-zinc-400" />
        <span className="text-zinc-100">{trip?.destination}</span>
      </div>

      <div className="flex items-center gap-5">
        <div className="flex items-center gap-2">
          <Calendar className="size-5 text-zinc-400" />
          <span className="text-zinc-100">{displayedDate}</span>
        </div>

        <div className="w-px h-6 bg-zinc-800" />

        <Button variant="secondary" onClick={openUpdateTripModal}>
          Alterar local/data
          <Settings2 className="size-5" />
        </Button>
      </div>

      {isUpdateTripModelOpen && (
        <UpdateTripModal trip={trip} onCloseModal={closeUpdateTripModal} />
      )}
    </div>
  )
}
