import { UserCog } from "lucide-react"
import { useState } from "react"
import { Activities } from "./activities"
import { CreateActivityModal } from "./create-activity-modal"
import { Guests } from "./guests"
import { ImportantLinks } from "./important-links"
import { TripDetailHeader } from "./trip-detail-header"
import { Button } from "../../components/button"
import { InviteGuestModal } from "./invite-guest-modal"

export function TripDetailsPage() {
  const [isCreateActivityModalOpen, setIsCreateActivityModalOpen] = useState(false)
  const [isInviteGuestModalOpen, setIsInviteGuestModalOpen] = useState(false)

  function openActivityModal() {
    setIsCreateActivityModalOpen(true)
  }

  function closeActivityModal() {
    setIsCreateActivityModalOpen(false)
  }

  function openInviteGuestModal() {
    setIsInviteGuestModalOpen(true)
  }

  function closeInviteGuestModal() {
    setIsInviteGuestModalOpen(false)
  }

  return (
    <div className="max-w-6xl px-6 py-10 mx-auto space-y-8">
      <TripDetailHeader />

      <main className="flex gap-16 px-4">
        <Activities onOpenActivityModal={openActivityModal} />

        {/* SIDE BAR */}

        <div className="w-80 space-y-6">
          <ImportantLinks />

          <div className="w-full h-px bg-zinc-400 shrink-0" />

          {/* CONVIDADOS */}

          <div className="space-y-6">
            <h2 className="font-semibold text-xl">Convidados</h2>

            <Guests />

            <Button
              variant="secondary"
              onClick={openInviteGuestModal}
              size="full">
              <UserCog className="size-5" />
              Gerenciar Convidados
            </Button>
          </div>
        </div>
      </main>

      {/* MODAL CREATE ACTIVITY */}
      {isCreateActivityModalOpen && (
        <CreateActivityModal onCloseActivityModal={closeActivityModal} />
      )}

      {/* MODAL INVITE NEW GUEST */}
      {isInviteGuestModalOpen && (
        <InviteGuestModal onCloseInviteGuestModal={closeInviteGuestModal} />
      )}
    </div>
  )
}
