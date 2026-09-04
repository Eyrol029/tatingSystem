const REPORT_HISTORY_KEY = 'tating-report-history'

export function recordReport({ name, type = 'Report', period = 'All time', details = '' }) {
  const existing = readReports()
  const report = {
    id: `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`,
    name,
    type,
    period,
    details,
    generatedAt: new Date().toISOString()
  }

  localStorage.setItem(REPORT_HISTORY_KEY, JSON.stringify([report, ...existing]))
  return report
}

export function readReports() {
  try {
    const saved = JSON.parse(localStorage.getItem(REPORT_HISTORY_KEY) || '[]')
    return Array.isArray(saved) ? saved : []
  } catch {
    return []
  }
}

export function removeReport(reportId) {
  localStorage.setItem(
    REPORT_HISTORY_KEY,
    JSON.stringify(readReports().filter(report => report.id !== reportId))
  )
}

export function clearReports() {
  localStorage.removeItem(REPORT_HISTORY_KEY)
}
