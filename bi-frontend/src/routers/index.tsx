import { createBrowserRouter } from 'react-router-dom'
import LoginPage from '../pages/User/Login'
import RegisterPage from '../pages/User/Register'
import AddChartPage from '../pages/Chart/AddChart'
import MyChartPage from '../pages/Chart/MyCharts'
import BatchChartPage from '../pages/Chart/BatchChart'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <LoginPage />,
  },
  {
    path: '/register',
    element: <RegisterPage />,
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/add_chart',
    element: <AddChartPage />,
  },
  {
    path: '/my_chart',
    element: <MyChartPage />,
  },
  {
    path: '/batch_chart',
    element: <BatchChartPage />,
  },
])
