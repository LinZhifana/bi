import Box from '@mui/material/Box'
import CssBaseline from '@mui/material/CssBaseline'
import AppBarComp from '../../../components/AppBar'
import DrawerComp from '../../../components/Drawer'
import { useEffect, useState } from 'react'
import { API } from '../../../services'
import { ChartVO } from '../../../services/Api'
import Dashboard from '../../../components/Dashboard'
import { Stack } from '@mui/material'
import MainComp from '../../../components/Main'

const MyChartPage: React.FC = () => {
  const [chartQueryRequest, setChartQueryRequest] = useState({
    pageNum: 1,
    pageSize: 10,
  })

  const [chartList, setChartList] = useState<ChartVO[]>([])

  useEffect(() => {
    const fetchPage = async () => {
      const res = await API.chart.getAllByUid(chartQueryRequest)

      const data = res.data

      if (data.code === 0) {
        if (typeof data.data === 'undefined') setChartList([])
        else setChartList([...data.data])
      } else if (data.code === 40000) {
        console.error('服务器故障!')
      }
    }

    fetchPage()
  }, [])

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBarComp />
      <DrawerComp />
      <MainComp>
        <Stack spacing={2}>
          {chartList.map((chartVO, index) => (
            <Dashboard key={index} {...chartVO} />
          ))}
        </Stack>
      </MainComp>
    </Box>
  )
}

export default MyChartPage
