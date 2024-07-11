import { ComponentProps, ReactNode } from "react"
import { tv, VariantProps } from "tailwind-variants"

const btnVariant = tv({
  // base = propriedades que serãoa iguais
  base: 'rounded-lg px-5 font-medium flex items-center justify-center gap-2 transition-colors',

  variants: {
    variant: {
      primary: 'bg-lime-300 text-zinc-950 hover:bg-lime-400 ',
      secondary: 'bg-zinc-800 text-zinc-200 hover:bg-zinc-700'
    },

    size: {
      default: 'py-2',
      full: 'h-11 w-full'
    }
  },

  // caso a variante não for informada
  defaultVariants: {
    variant: 'primary',
    size: 'default'
  }
})

interface ButtonProps extends ComponentProps<"button">, VariantProps<typeof btnVariant> {
  children: ReactNode
}

export function Button({ children, size, variant, ...props }: ButtonProps) {
  return (
    <button
      {...props}
      className={btnVariant({ variant, size })}>
      {children}
    </button>
  )
}
