# Psyche Assistant Changelog
- - -

> ### Author: Mads Søndergaard 
> ### Current version: 0.1.0
> ### License: AGPL
- - -

## Version history:
### 0.1.0:
> General
> - Begun development of user integration, using external database (Spring Boot/H2) for general data storage
> - Minor refactoring, but primarily avoiding legacy proof-of-concept code for the time being.

> Android
> - Begun development of security integration, using MMKV for Android specific secure storage.

### 0.0.5:
> General
> - Finetuned results page for better clarity in terms of result and user understanding.
> - Begun reconfiguring architecture for better clarity, as well as added code documentation

### 0.0.4:
> General
> - Refactored scoring system so it is more in line with logical use-flow; subtracting and adding scores as one might expect.
> - Added result view and called this from the Survey page when Questions have been answered.
> - Closed logical holes in code so the app no longer crashes if the user tries to go outside the range of the question list.
> - Added relevant symptom and awareness info in English and Danish.

### 0.0.3:
> General
> - Worked on translation functionality so contents adheres to device language
>   - But added the option for survey questions to be translateable, as these require separate translator (since they are stored in JSON, not Resources)

### 0.0.2:
> General
> - Various fine tunement of views and logical structure for ease of use
> - Ensured score is updated based on selected value moving forward and backward
> - Ensured value per question is reflected in total sum of values for all questions.
> - Various considerations in terms of:
>   - Translation; dynamic vs static
>   - User interface; readability and clarity
>   - Database; local vs external, on-premise vs cloud.
> - Added helper function for converting float to decimal, as this would otherwise not be available in Kotlin, only natively in Java (Android)

> Android
> - N/A

> iOS
> - N/A


### 0.0.1:
> General
> - Decided on licensing format; AGPL 
> - Added serializable Question object for iterating across JSON content.
> - Populated JSON file with placeholder questions.
> - Setup primitive Android view that can display each Question object
>   - And react according to the question-type
> - Added slider for more granular and less taxing indication of severity during assessment.


> Android
> - Added functionaltiy for reading JSON file from local storage.

> iOS
> - N/A