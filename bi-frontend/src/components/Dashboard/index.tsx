import Card from '@mui/material/Card'
import CardHeader from '@mui/material/CardHeader'
import CardContent from '@mui/material/CardContent'
import Typography from '@mui/material/Typography'
import { ChartVO } from '../../services/Api'
import ReactECharts from 'echarts-for-react'

const Dashboard: React.FC<ChartVO> = (chart: ChartVO) => {
  const parse = (option: string | undefined) => {
    if (typeof option === 'undefined') return {}
    console.log(chart)
    try {
      return JSON.parse(option)
    } catch (error) {
      console.error('Failed to parse option:', error)
      return {}
    }
  }

  return (
    <Card>
      <CardHeader title={chart.name} subheader={chart.chartType} />
      <CardContent
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
        }}>
        <ReactECharts
          option={parse(chart.genChart)}
          style={{ height: 540, width: 960 }}
        />
      </CardContent>

      <CardContent>
        <Typography paragraph>{chart.goal}</Typography>
        <Typography variant="body2" color="text.secondary">
          {chart.genResult}
        </Typography>
      </CardContent>
    </Card>
  )
}

export default Dashboard
