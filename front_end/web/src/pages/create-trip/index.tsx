import { FormEvent, useState } from "react"
import { DateRange } from "react-day-picker"
import { useNavigate } from "react-router-dom"
import { api } from "../../lib/axios"
import { ConfirmTripModal } from "./confirm-trip-modal"
import { InviteGuestsModal } from "./invite-guests-modal"
import { DestinationAndDateStep } from "./steps/destination-and-date-step"
import { InviteGuestsStep } from "./steps/invite-guests-step"
import { TripRequest } from "../../model/Trip"

// name export -> evita importações com nomes errados
export function CreateTripPage() {
  const [isGuestsInputOpen, setIsGuestsInputOpen] = useState(false)
  const [isGuestsModalOpen, setIsGuestsModalOpen] = useState(false)
  const [isConfirmTripModalOpen, setIsConfirmTripModalOpen] = useState(false)
  const [emailsToInvite, setEmailsToInvite] = useState<string[]>([])

  const [destination, setDestination] = useState("")
  const [eventStartAndEndDates, setEventStartAndEndDates] = useState<DateRange | undefined>()
  const [ownerName, setOwnerName] = useState("")
  const [ownerEmail, setOwnerEmail] = useState("")

  const navigate = useNavigate()

  function openGuestsInput() {
    setIsGuestsInputOpen(true)
  }

  function closeGuestsInput() {
    setIsGuestsInputOpen(false)
  }

  function openGuestsModal() {
    setIsGuestsModalOpen(true)
  }

  function closeGuestsModal() {
    setIsGuestsModalOpen(false)
  }

  function openConfirmTripModal() {
    setIsConfirmTripModalOpen(true)
  }

  function closeConfirmTripModal() {
    setIsConfirmTripModalOpen(false)
  }

  function addNewEmailToInvite(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    const data = new FormData(event.currentTarget)
    const email = data.get("email")?.toString()

    if (!email) {
      // TODO - ALERT
      return
    }

    if (emailsToInvite.includes(email)) {
      // TODO - ALERT
      return
    }

    setEmailsToInvite([...emailsToInvite, email])

    event.currentTarget.reset()
  }

  function removeEmailFromInvites(email: string) {
    const newEmailList = emailsToInvite.filter(invited => invited !== email)

    setEmailsToInvite(newEmailList)
  }

  async function createTrip(event: FormEvent<HTMLFormElement>) {
    event.preventDefault()

    // NOTE - ALERT
    if (!destination) {
      return
    }

    if (!eventStartAndEndDates?.from || !eventStartAndEndDates?.to) {
      return
    }

    if (emailsToInvite.length === 0) {
      return
    }

    if (!ownerName || !ownerEmail) {
      return
    }

    const request: TripRequest = {
      destination,
      starts_at: eventStartAndEndDates.from.toISOString(),
      ends_at: eventStartAndEndDates.to.toISOString(),
      emails_to_invite: emailsToInvite,
      owner_name: ownerName,
      ownerEmail
    }

    const response = await api.post("/trips", request)

    const { trip_id } = response.data
    navigate(`/trips/${trip_id}`)
  }

  return (
    <div className="h-screen flex items-center justify-center bg-pattern bg-no-repeat bg-center">
      <div className="max-w-3xl w-full px-6 text-center space-y-10">
        <div className="flex flex-col items-center gap-3">
          <img
            src="/logo.svg"
            alt="plann.er"
          />
          <p className="text-zinc-300 text-lg">Convite seus amigos e planeje sua próxima viagem!</p>
        </div>

        <div className="space-y-4">
          {/* CAMPOS DE DESTINO E DATA DA VIAGEM */}

          <DestinationAndDateStep
            isGuestsInputOpen={isGuestsInputOpen}
            onCloseGuestsInput={closeGuestsInput}
            onOpenGuestsInput={openGuestsInput}
            onSetDestination={setDestination}
            eventStartAndEndDates={eventStartAndEndDates}
            onSetEventStartAndEndDates={setEventStartAndEndDates}
          />

          {/* CAMPO QUE ABRE O MODAL PARA CONVIDAR PARTICIPANTES*/}
          {/* E BOTÃO QUE ABRE O MODAL DE CONFIRMAÇÃO DE VIAGEM */}

          {isGuestsInputOpen && (
            <InviteGuestsStep
              emailsToInvite={emailsToInvite}
              onOpenConfirmTripModal={openConfirmTripModal}
              onOpenGuestsModal={openGuestsModal}
            />
          )}
        </div>

        <p className="text-sm text-zinc-500">
          Ao planejar sua viagem pela plann.er você automaticamente concorda <br /> com nossos{" "}
          <a
            href="#"
            className="text-zinc-300 underline">
            {" "}
            termos de uso{" "}
          </a>{" "}
          e{" "}
          <a
            href="#"
            className="text-zinc-300 underline">
            {" "}
            políticas de privacidade{" "}
          </a>
        </p>
      </div>

      {/* MODAL PARA CONVIDAR PARTICIPANTES */}

      {isGuestsModalOpen && (
        <InviteGuestsModal
          onCloseGuestsModal={closeGuestsModal}
          emailsToInvite={emailsToInvite}
          onRemoveEmailFromInvites={removeEmailFromInvites}
          onAddNewEmailToInvite={addNewEmailToInvite}
        />
      )}

      {/* MODAL DE CONFIRMAÇÃO DA VIAGEM */}

      {isConfirmTripModalOpen && (
        <ConfirmTripModal
          onCreateTrip={createTrip}
          onCloseConfirmTripModal={closeConfirmTripModal}
          onSetOwnerName={setOwnerName}
          onSetOwnerEmail={setOwnerEmail}
        />
      )}
    </div>
  )
}
