{{/*
Expand the name of the chart.
*/}}
{{- define "prox-backend.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "prox-backend.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "prox-backend.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "prox-backend.labels" -}}
prox-backend.sh/chart: {{ include "prox-backend.chart" . }}
{{ include "prox-backend.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "prox-backend.selectorLabels" -}}
app.kubernetes.io/name: {{ include "prox-backend.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "prox-backend.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "prox-backend.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Looks if there's an existing secret and reuse its password. If not it generates a new password and uses it.
*/}}
{{- define "prox-backend.superUserPassword" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace (include "prox-backend.fullname" .) ) -}}
  {{- if $secret -}}
    {{-  index $secret "data" "superUserPassword" -}}
  {{- else -}}
    {{- (randAlphaNum 40) | b64enc | quote -}}
  {{- end -}}
{{- end -}}

{{/*
Looks if there's an existing secret and reuse its password. If not it generates a new password and uses it.
*/}}
{{- define "prox-backend.replicationUserPassword" -}}
{{- $secret := (lookup "v1" "Secret" .Release.Namespace (include "prox-backend.fullname" .) ) -}}
  {{- if $secret -}}
    {{-  index $secret "data" "replicationUserPassword" -}}
  {{- else -}}
    {{- (randAlphaNum 40) | b64enc | quote -}}
  {{- end -}}
{{- end -}}
