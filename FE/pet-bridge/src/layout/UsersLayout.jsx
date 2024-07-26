import {Outlet} from "react-router-dom"
import Navbar from "components/header/Navbar"

const UsersLayout = () => {
  return (
    <div>
      <Navbar />
      <section className="fixed left-1/2 top-1/2 flex -translate-x-1/2 -translate-y-1/2 flex-col items-center">
        <Outlet className="w-[1000px]"></Outlet>
      </section>
    </div>
  )
}

export default UsersLayout
