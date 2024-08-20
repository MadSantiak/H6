# Psyche Assistant Changelog
- - -

> ### Author: Mads Søndergaard 
> ### Current version: 0.2.0
> ### License: AGPL
- - -

## Version history:
### 0.2.0:
> General
> - Moved to major change as the application now fulfills all major primary milestones
> - Added visualization of energy consumption on a weekly basis
> - Minor adjustment to view definitions to ensure scrolling was available on pages where it would likely be necessary.

### 0.1.6:
> General
> - Worked on finishing touches for activity integration, i.e. fetching completed activities, deleting and completing them
> - Worked on integrating activity integration into new and existing views for better clarity
>   - Note that the User models "energyExpenditure" attribute is now deprecated, as it is instead extrapolated
>     from the summation of activities for the day/period.
> - Slight refactoring of views so that MainPage now has sections for both Energy Consumption and Survey History
> - And the above is more clearly reflected in the code structure.

### 0.1.5:
> General
> - Fleshed out activity-based views, including dialogs for creation, precursor to list views, etc.
> - Refactored project structure, including the structure belonging to the proof-of-concept version of the project.
> - Corrected some of the API communication in terms of how data is processed front-end before being sent to back-end
>   - Note the single-source-of-truth is kept in Backend, but verification is still carried out in front-end


### 0.1.4:
> General
> - Tested various use cases for logical holes, patched in both front- and back-end.
> - Refactored views so file structure is more readable and easier to maintain/edit
> - Improved clarity on UI/UX in terms of user overview for groups, navigation, and the like.
> - Further implemented Activity model and relation to other models (User/Group)

### 0.1.3:
> General
> - Further polish to UI/UX
> - Implemented logic for group owner management of users (remove user)
> - Implemented logic for joining groups
> - Consolidated Group and User setting pages to a single Account settings page.
> - Moved hard-coded string values to resources.
> - Made various adjustments to layout
> - Added error handling on app start-up, in case server is unavailable. Still allowing the user to take surveys if that is the case.

### 0.1.2:
> General
> - Refactoring Controller/Repository structure so delegation is now:
>   - Controller: Used to control the action between the user and app
>   - Service: Used by the controller to communicate between the app and server
>   - Repository: Used to act as a contract for what functions are available to the Service.
> - Reconfigured model so front-end now deals with ID-based relations. For the sake of ease of use, backend still operates with object based relations.
> - General refactoring and implementation of code to assist in ongoing debugging and testing.
> - Added additional common-screens to help indicate use flow and state (loading screen, error screen)

> Android
> - Configured logo for the app. 

### 0.1.1:
> General
> - Migrated to IntelliJ 2024.2 in order to fix issue with debugging suspend functions
>   - This, however, has caused a minor annoyance with physical device screen mirroring not functioning. 
>     - [X] Reported to JetBrains.
> - Added base model description for Group entity.
> - Moved User settings to Settings page.

### 0.1.0:
> General
> - Begun development of user integration, using external database (Spring Boot/H2) for general data storage
> - Minor refactoring, but primarily avoiding legacy proof-of-concept code for the time being.

> Android
> - Begun development of security integration, using MMKV for Android specific secure storage.

### 0.0.5:
> General
> - Fine tuned results page for better clarity in terms of result and user understanding.
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