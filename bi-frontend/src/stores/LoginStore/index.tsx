import { create } from 'zustand'
import { UserVO } from '../../services/Api'

type LoginStore = {
  user: UserVO | null
  login: (user: UserVO) => void
  logout: () => void
}

export const useLoginState = create<LoginStore>()((set) => ({
  user: null,
  login: (user: UserVO) => set(() => ({ user: user })),
  logout: () => set(() => ({ user: null })),
}))
