## Changes in mnalis-helium314 branch

This file details changes made in https://github.com/mnalis/StreetComplete/tree/mnalis
as modified from upstream https://github.com/streetcomplete/StreetComplete

From `mnalis` branch:
* automatic gihub build Actions
  to get artifact .apk, so we don't need local android build environment
* update email/github links to this repository
  so upstream is not bothered with bugs introduced here
* show ALL notes
  when `Show all notes` in preferences is enabled, really **show ALL notes**,
  including your own (only hide notes that user explicitely asked to hide,
  and which can be unhidden via `Restore hidden quests` preference)

Cherry-picked from `helium314` branch (from https://github.com/Helium314/StreetComplete/tree/mods):
* rename app (commit 70a2b3be92533f05beadb83e0a66b07ec6bebfde)
* add quest profiles (commit 3d97a952d355e2ef4b4ee3c6db0670cda60f5876)
* add phone number and website quests (commit a01c492bdc1f7385a7b83acd59fed5100fc55cdf)
* adjust dark theme (commit 3251e335c7839520a2eb95b120d6ad88d333f19c)
* move "none" answer for cycleway to more convenient position (commit 4812c692041d3c3b5ccd4dbbe249132c107317e1)


## TODO
* check https://github.com/Helium314/StreetComplete/tree/mods changes

  See https://github.com/streetcomplete/StreetComplete/discussions/3003#discussioncomment-963592

  Not included yet:
    * add poi "quests" and adjust quest dot offset (commit 2ea50a4433d9db5b5101a739efd4c9e4282959fc)
    * add gpx notes (commit a01bc6a2ba54fca1c791237571d6b08a4f1ceb2e)
    * add button to reverse quest order (commit 1982e79ef618444bc41cb609f9d5270805a24ab2)
    * auto-download only if auto-upload is allowed (commit 52cf846a7544c5c1fbf39f1ef0cae8eb93b945ab)

* get icon for phone quest from atrate branch?
  to update `helium314` quest.
  Maybe even check `atrate` code for such quest and merge/replace?

* allow house address quest even if no building type ?
  See [issue #2464](https://github.com/streetcomplete/StreetComplete/issues/2464)
* add language selector (or force language to HR in this fork if it is much simpler)
  See https://github.com/streetcomplete/StreetComplete/issues/2964#issuecomment-862609036 and related issues.
* fix helium314 presets crashing on initial setup?
* replace helium314 reverse quest order button with quick preset change button?
* check https://github.com/matkoniecz/Zazolc ?
* check https://github.com/streetcomplete/StreetComplete/compare/master...pietervdvn:master ?
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
  https://docs.github.com/en/actions/learn-github-actions/security-hardening-for-github-actions
  https://coletiv.com/blog/android-github-actions-setup/
  https://riggaroo.dev/using-github-actions-to-automate-our-release-process/
  https://medium.com/google-developer-experts/github-actions-for-android-developers-6b54c8a32f55
* implement quest for air compressor
  https://github.com/streetcomplete/StreetComplete/issues/3053
* fix images in Notes
  can't save them in helim314 branch, probably due to path changes?
* house type quest, allow caching Residental/Commercial too
