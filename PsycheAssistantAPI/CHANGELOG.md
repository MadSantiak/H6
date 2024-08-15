# Psyche Assistant API Changelog
- - - 

> ### Author: Mads Søndergaard
> ### Current version: 0.0.6
> ### License: AGPL
- - -

## Version history:
### 0.0.6:
> General
> - Fleshed out Activity based logic (Controller, Service, Repository), and aligned front-end with API.
> - Added functionality for fetching Activities based on Group
> - Added functionality for creating an Activity.
> - Added functionality for deleting/completing activities, albeit untested at this point.

### 0.0.5:
> General
> - Fine-tuned certain use cases such as removal of users
> - Patched logical hole which caused recursion when fetching users/groups
> - Implemented more logic related to Activities.
 
### 0.0.4:
> General
> - Polished user manipulation of group content
> - Added helper method for authentication
> - Various smaller adjustments to logic as needed.

### 0.0.3:
> General
> - Worked on aligning de/serialization between backend and frontend in a way that makes technical sense.
> - Refactored code to be in line with this approach; backend uses full-model relations, while frontend uses ID-relations.

### 0.0.2:
> General
> - Added foundation for Group model (Entity, Repository, Controller)
> - Various minor tweaks.
 

### 0.0.1:
> General
> - Initial commit
> - Included fundamental structure for model (Entity, Repository, Controller)
> - Using Spring Boot API and H2 storage
> - Using JWT, begun implementation of security logic for safe access from application based on user-authentication.

> Note:
> - Opted to use JWT rather than, for example, Firebase, to avoid lock-in.