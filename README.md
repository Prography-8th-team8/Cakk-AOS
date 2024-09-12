# Cakk
Cake shop information provision service based on user location

![스크린샷 2023-10-29 오후 11 58 45](https://github.com/user-attachments/assets/dfc680db-b19e-42b4-b197-6d124ab81c5a)

<br>

## Download
Go to the [Release](https://play.google.com/store/apps/details?id=com.prography.cakk&hl=ko&gl=US) to download the latest APK.
But, the service was terminated due to lack of server maintenance.
<br>

## Tech stack & Open-source libraries
- Min SDK 26
- Kotlin based, Coroutines + Flow for asynchronous.
- Jetpack
  - Compose
  - Lifecycle
  - ViewModel
  - Room
  - Hilt
  - DataStore
  - Navigation
  - Paging
- Coil
- Ktor & OkHttp3 & Gson
- 3rd party library
  - Lottie
  - Timber
  - Naver-map

<br>

## Architecture
Cakk is based on a clean architecture, and the presentation applies the MVI + Redux pattern.

![서비스 구조도 Android 3](https://github.com/user-attachments/assets/f4c7bf9f-4a3a-4465-8cc4-cb41732ec6b1)

<br>

## Modularization

![Untitled](https://github.com/user-attachments/assets/ae9c35a7-bed5-4789-b9a1-36d8bfe7de15)
- app : Entry Point
- core
  - base : base module
  - designsystem : compose ui component
  - localdb : local datasource like datastore, room db
  - network : remote datasource using api 
  - utility : mapper, logger ..
- common : navigation
- data : business logic, repositoryImpl
- domain : repository, entity
- feature
  - onboarding
  - home
  - feed
  - my
  - splash
 
<br>

# License
```
Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```
