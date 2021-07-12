## Changes in mnalis branch

This file details changes made in https://github.com/mnalis/StreetComplete/tree/mnalis 
as modified from upstream https://github.com/streetcomplete/StreetComplete

* automatic gihub build Actions
  to get artifact .apk, so we don't need local android build environment
* update email/github links to this repository
  so upstream is not bothered with bugs introduced here
* show ALL notes
  when `Show all notes` in preferences is enabled, really **show ALL notes**,
  including your own (only hide notes that user explicitely asked to hide,
  and which can be unhidden via `Restore hidden quests` preference)

## TODO
* check https://github.com/Helium314/StreetComplete/tree/mods changes
  See https://github.com/streetcomplete/StreetComplete/discussions/3003#discussioncomment-963592
* allow house address quest even if no building type ?
  See [issue #2464](https://github.com/streetcomplete/StreetComplete/issues/2464)
* add language selector (or force language to HR in this fork if it is much simpler)
  See https://github.com/streetcomplete/StreetComplete/issues/2964#issuecomment-862609036 and related issues.
* check https://github.com/matkoniecz/Zazolc ?
* https://github.com/Atrate/CompleteStreet
    * show more lit quests
    * show more surface quests
* make GitHub build signed version, so upgrade does not mean uninstalling and losing all preferences
  https://medium.com/upday-devs/how-to-setup-github-actions-for-android-projects-a94e8e3b0539
  https://danielllewellyn.medium.com/flutter-github-actions-for-a-signed-apk-fcdf9878f660
  https://github.com/actions/upload-release-asset
  https://github.com/actions/create-release
  https://github.com/ShaunLWM/action-release-debugapk
  https://github.com/ad-m/github-push-action
  https://github.com/r0adkll/sign-android-release
  https://github.com/marketplace/actions/sign-android-release
  https://github.com/marketplace/actions/android-sign
