import { LucideProps } from "lucide-react";

export function Icon({ icon: Icon }: { icon: React.FC<LucideProps> }) {
  return <Icon className="side-5 text-zinc-400" />;
}