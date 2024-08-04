import Box from '@mui/material/Box'
import CssBaseline from '@mui/material/CssBaseline'
import AppBarComp from '../../../components/AppBar'
import DrawerComp from '../../../components/Drawer'
import SendIcon from '@mui/icons-material/Send'
import CloudUploadIcon from '@mui/icons-material/CloudUpload'
import {
  Alert,
  Button,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  styled,
  TextField,
  Typography,
} from '@mui/material'
import { useState } from 'react'
import MainComp from '../../../components/Main'
import { ChartVO, DoChatRequest } from '../../../services/Api'
import LoadingButton from '@mui/lab/LoadingButton'

import { API } from '../../../services'
import Dashboard from '../../../components/Dashboard'

const VisuallyHiddenInput = styled('input')({
  clip: 'rect(0 0 0 0)',
  clipPath: 'inset(50%)',
  height: 1,
  overflow: 'hidden',
  position: 'absolute',
  bottom: 0,
  left: 0,
  whiteSpace: 'nowrap',
  width: 1,
})

const alertState = {
  default: 0,
  file_error: 1,
  param_error: 2,
  success: 3,
  fail: 4,
}

const MainCard: React.FC = () => {
  const [chartType, setChartType] = useState('')
  const [name, setName] = useState('')
  const [question, setQuestion] = useState('')
  const [file, setFile] = useState<File | null>(null)

  const [option, setOption] = useState('')
  const [message, setMessage] = useState('')

  const [loading, setLoading] = useState(false)

  const [alert, setAlert] = useState(alertState.default)

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const files = event.target.files
    if (!files || files.length === 0) {
      setAlert(alertState.file_error)
      return
    }

    const selectedFile = files[0]
    if (
      selectedFile &&
      selectedFile.type ===
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    ) {
      setFile(selectedFile)
    } else {
      setAlert(alertState.file_error)
    }
  }

  const showAlert = () => {
    if (alert === alertState.default) {
      return <></>
    }
    if (alert === alertState.file_error) {
      return <Alert severity="error">上传文件错误</Alert>
    }
    if (alert === alertState.param_error) {
      return <Alert severity="error">*为必填项</Alert>
    }
    if (alert === alertState.fail) {
      return <Alert severity="error">网络故障</Alert>
    }
  }

  const handleSubmit = async () => {
    if (file === null || name === '' || question === '') {
      setAlert(alertState.param_error)
      return
    }

    setLoading(true)

    const doChatRequest: DoChatRequest = {
      name: name,
      question: question,
      data: '',
      chatType: chartType,
    }

    const formData = new FormData()
    formData.append('file', file)
    formData.append(
      'doChatRequest',
      new Blob([JSON.stringify(doChatRequest)], { type: 'application/json' })
    )

    try {
      const res = await API.instance.post('/chart/generate', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      })

      const data = res.data

      if (data.code === 0) {
        setOption(data.data?.option as string)
        setMessage(data.data?.message as string)
      } else if (data.code === 40000) {
        setAlert(alertState.fail)
      }
    } catch (error) {
      setAlert(alertState.fail)
    } finally {
      setLoading(false)
    }
  }

  const getChartVO = () => {
    return {
      name: name,
      chartType: chartType,
      goal: question,
      genChart: option,
      genResult: message,
    } as ChartVO
  }

  return (
    <MainComp>
      <Grid container spacing={4}>
        <Grid item xs={12}></Grid>
        <Grid item xs={6}>
          <TextField
            id="outlined-basic"
            label="图表名称"
            value={name}
            variant="outlined"
            fullWidth
            onChange={(e) => {
              setName(e.target.value)
              setAlert(alertState.default)
            }}
            required
          />
        </Grid>

        <Grid item xs={6}>
          <FormControl fullWidth>
            <InputLabel id="chart-type-select-label">图标类型</InputLabel>
            <Select
              labelId="chart-type-select-label"
              id="chart-type-select"
              value={chartType}
              label="图标类型"
              onChange={(e) => {
                setChartType(e.target.value)
                setAlert(alertState.default)
              }}>
              <MenuItem value={'折线图'}>折线图</MenuItem>
              <MenuItem value={'柱状图'}>柱状图</MenuItem>
              <MenuItem value={'饼图'}>饼图</MenuItem>
              <MenuItem value={'散点图'}>散点图</MenuItem>
              <MenuItem value={''}>什么都不选</MenuItem>
            </Select>
          </FormControl>
        </Grid>

        <Grid item xs={12}>
          <TextField
            id="outlined-basic"
            label="需要解决的问题/目标"
            variant="outlined"
            fullWidth
            value={question}
            onChange={(e) => {
              setQuestion(e.target.value)
              setAlert(alertState.default)
            }}
            required
          />
        </Grid>
        <Grid item xs={6}>
          <Button
            component="label"
            role={undefined}
            variant="contained"
            tabIndex={-1}
            startIcon={<CloudUploadIcon />}>
            Upload file
            <VisuallyHiddenInput
              type="file"
              accept=".xlsx"
              onChange={(e) => {
                handleFileChange(e)
                setAlert(alertState.default)
              }}
            />
          </Button>
          <Typography variant="caption" display="block">
            上传excel(.xlsx)数据
          </Typography>
        </Grid>
        <Grid item xs={6}>
          <LoadingButton
            size="small"
            onClick={handleSubmit}
            endIcon={<SendIcon />}
            color="success"
            loading={loading}
            loadingPosition="end"
            variant="contained">
            <span>Confirm</span>
          </LoadingButton>

          <Typography variant="caption" display="block">
            进行AI分析
          </Typography>
        </Grid>

        <Grid item xs={12}>
          {showAlert()}
        </Grid>

        <Grid item xs={12}>
          {/* <ChartCard option={option} message={message} /> */}

          <Dashboard {...getChartVO()} />
        </Grid>
      </Grid>
    </MainComp>
  )
}

const AddChartPage: React.FC = () => {
  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBarComp />
      <DrawerComp />
      <MainCard />
    </Box>
  )
}

export default AddChartPage
