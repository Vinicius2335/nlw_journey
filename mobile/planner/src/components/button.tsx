import { Text, TextProps, TouchableOpacity, TouchableOpacityProps, ActivityIndicator } from "react-native"
import clsx from "clsx"
import { createContext, useContext } from "react"

type Variants = "primary" | "secondary"

type ButtonProps = TouchableOpacityProps & {
  variant?: Variants
  isLoading?: boolean
}

// Contexto que existe somente para esse componente
// assim Title tbm poderá ter a acesso a variante e aplicar os estilos de acordo com a opção selecionada 
// Passo 1
const ThemeContext = createContext<{variant ?: Variants}>({})

function Button({ children, isLoading, className, variant = "primary", ...props }: ButtonProps) {
  return (
    <TouchableOpacity
      className={clsx("h-11 flex-row items-center justify-center rounded-lg gap-2 px-2", {
        "bg-lime-300": variant === "primary",
        "bg-zinc-800": variant === "secondary"
      }, className)}
      activeOpacity={0.7}
      disabled={isLoading}
      {...props}>

      {/* Passo 2 */}
      <ThemeContext.Provider value={{variant}}>
        { isLoading ? <ActivityIndicator className="text-lime-950" /> : children}
      </ThemeContext.Provider>

    </TouchableOpacity>
  )
}

function Title({ ...props }: TextProps) {
  // Passo 3
  const { variant } = useContext(ThemeContext)

  return (
    <Text
      className={clsx(
        "text-base font-semibold",
        {
          "text-lime-950": variant === "primary",
          "text-zinc-200": variant === "secondary"
        }
      )}
      {...props}></Text>
  )
}

Button.Title = Title

export { Button }
