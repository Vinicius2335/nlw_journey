import { X } from "lucide-react"
import { FormEvent, ReactNode } from "react"
import { Button } from "./button"

interface CustomModalProps {
  onCloseModal: () => void
  onClickButton: (event: FormEvent<HTMLFormElement>) => void
  children: ReactNode
  title: string
  describe: string
  btnDescribe: string
}

export function CustomModal({
  onCloseModal,
  onClickButton,
  title,
  describe,
  btnDescribe,
  children
}: CustomModalProps) {
  return (
    <div className="fixed inset-0 bg-black/60 flex items-center justify-center">
      <div className="w-[540px] rounded-xl py-5 px-6 shadow-shape bg-zinc-900 space-y-5">
        <div className="space-y-2">
          <div className="flex item-center justify-between">
            <h2 className="text-lg font-semibold">{title}</h2>
            <button onClick={onCloseModal}>
              <X className="side-5 text-zinc-400" />
            </button>
          </div>

          <p className="text-sm text-zinc-400">{describe}</p>
        </div>

        <form
          onSubmit={onClickButton}
          className="space-y-3">

          {children}

          <Button
            type="submit"
            size="full">
            {btnDescribe}
          </Button>
        </form>
      </div>
    </div>
  )
}
