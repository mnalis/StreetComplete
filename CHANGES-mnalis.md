## Changes in mnalis-v42b1-helium314 branch

This file details changes made in https://github.com/mnalis/StreetComplete/tree/mnalis-v38
as modified from upstream https://github.com/streetcomplete/StreetComplete

From `mnalis-v42b1` branch:
* update email/github links to this repository
  so upstream is not bothered with bugs introduced here
* show ALL notes
  when `Show all notes` in preferences is enabled, really **show ALL notes**,
  including your own (only hide notes that user explicitely asked to hide,
  and which can be unhidden via `Restore hidden quests` preference)
* allow disabling/moving Notes quest too
  https://github.com/streetcomplete/StreetComplete/issues/3532
* ask for backrest on leisure=picnic_table too
  from `picnic-backrest` branch (see https://github.com/streetcomplete/StreetComplete/pull/3521)
* show more lit quests, and more surface quests
  from https://github.com/Atrate/CompleteStreet
* rename app to `de.westnordost.streetcomplete.mn`
* keep .apk files in build artifacts for 3 months instead of 1 day
* put Notes photo button to the right, for right-handed one-hand operation
* ask for sidewalks more often (even if one separately mapped footway is nearby)
* ask cycleway on residential streets without maxspeed too
  https://github.com/streetcomplete/StreetComplete/issues/3519
* rearrange quest order, and enable by default some previously disabled quests
* revert and update design of main menu, as to not reduce UI usability
  https://github.com/streetcomplete/StreetComplete/discussions/4130
* autosync off by default
* make GitHub build signed version, so upgrade does not mean uninstalling and losing all preferences (W.I.P.?)
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

From `upstream-helium314-new` (and `upstream-helium314-old`?) branches:
* move "no cycleway" answer to different position  (commit 1af6ce536211bd005a927414b372aa3c360f0620)

## TODO
* FIXME all "mnalis:" and "/mn/" commits in mnalis-everything but not in mnalis-v42b1
  (and move that all as PRs for Helium314 SC-EE branch!)
* update APK build code, and put date/version/last commit in filename.
  Also, is it possible to download apk directly from actions, or must it be packed in debug.zip ?
* showing git ref Version in code
  see https://stackoverflow.com/questions/17097263/automatic-versioning-of-android-build-using-git-describe-with-gradle#18021756
* Cherry-pick stuff from `helium314` branch (from https://github.com/Helium314/StreetComplete/tree/mods) (NOTE: there are probably newer version of this patches)
  See https://github.com/streetcomplete/StreetComplete/discussions/3003#discussioncomment-963592
  Newer: https://github.com/Helium314/StreetComplete/blob/my_modifications/README.md ?

  Not included yet (FIXME: needs checking newest branch and documenting; also sometimes older commits are sometimes better as it asks in more situations for website etc):
    * add phone number and website quests (commit a01c492bdc1f7385a7b83acd59fed5100fc55cdf)
    * add poi "quests" and adjust quest dot offset (commit 2ea50a4433d9db5b5101a739efd4c9e4282959fc) -- add 'FIXME' quest only, but not offsets etc
    * add 'hide' button  (commit 87fce03ec2d2efaa578920eb79da179c24c86587)
    * add level quests for shopping malls  (commit b2ae6032809b25f206daaf1deb79fe98693e8198)
    * update contact quests  (commit 2dddd0b2d49ffa6c5dc2d67bd629cfcbce8537c5) -- FIXME: it also removes key_cutter, hostel etc, why?
    * adjust dark theme (commit 3251e335c7839520a2eb95b120d6ad88d333f19c)
    * add per-quest settings for some quests (for element selection) (commit a60a389024f7db64091b25c950e511180c3a5ff9)
    * add maxspeed tag selection to per-quest-settings (commit e256a94617d6465db3a6cef92b89ef83e6665d35)
    * x 148a60f1b remove ShowFixme() and other Show() which are in other Helium314 patches we didn't import
    * replace helium314 reverse quest order button with quick preset change button?

    * also make buildings on light theme more transparent  (commit edd770106132915823829b5da4aacf1a434ef651)
    * make buildings more transparent (dark mode only) (commit 367b904990f1824eb2c114ec48ef44f1393afd1b)
    * disable 3D (commit f379fc78376212362d47f9efa3b6b7692031bfcf, 08a791676728d683849938f8a7c03a2bbee92131, 825afd551347cb1add9446d72c5df896e98893e8)
    * update icons  (commit bce1e5569137bfdef5468d8f861c6363c70aec5b)
    * update level filter  (commit c19129b8b86f810bfdfca3c77c6edbdbc4c98bd1)
    * add raised crossing answer to show poi quest, remove unused imports  (commit cdf7fa5aaf05ac289cb356e314bbf5ffc5f6517f, 5c9a07257d30184475d92df2908180ff0e159d9c)
    * fix build  (commit 1bdad058165b769020b611b5449ccc96ad027f21, a2cb3c68e2927fcaf90f4ab1515428ef58547c15)
    * add quest asking for cuisine of fast food and restaurants  (commit c8d36d64595885921b220207b82fe2f20a4d1a22)
    * level quest: don't ask for parking and parking entrance  (commit 0a6e59a2ea786f9f76ac15241da703917171b79f)
    * level quest: ask for all buildings (commit 3a7b050b6591f37e56c44647b6209eb161be304f)
    * change contact quests to use the same list (commit 204824e0815eabf35bed6b780df5f0216b878851)
    * make day/night filter optional, add setting (commit 965a9427c4e0cf344cfd0e809fb9f43639da1bee)
    * fix level quest (131831b34f8f8d34477b61ce4031e910c9d5e8ab)
    * update level quest (requires at least one poi with tagged level) (commit 7458bfe43e46d6dd89a28b09bb7bf2e361256f85)
    * add "zebra" answer for crossing type quest (commit 68f49d281aed27d3b526fd35d5552a9095f43036)
    * extend map tile cache from 12 hours to 8 days  (commit 365b097d3349423d2777cec7238faa115155c14a)
    * add level filter (commit 4dbc75e59863e3d3e96015bf8eed736459483387)
    * small update to place level quest (commit 21d85b8395494f8c4751c9bdd9eb036b0f3a3881)
    * don't ask level if addr:floor exists (727208b44d94605801780fc1672e8eaa3d2f840c)
    * add barn, cowshed, stable, sty and transformer tower building types (commit aee087228fd7ed7bacea90edf947240ba08cf530)
    * add "private" other answer to track, lit and surface quests (commit bf7a8771c2480f10813daef25f06f92a4c5bc649)
    * add "demolished" other answer for building type (commit 844e027754c5d48a1c9d214318813dc7f88f1015)
    * add quests asking for level inside mall or retail buildings (commit 1dd8d52e87a25f8608d6479eedba6675fc23beb3)
    * add phone and website quests (commit 8e65bacde415860fd3e29a402d3cd9056a4e187f)
    * show pois and adjust quest dot and pin offsets (commit 8cf33fab948a3c3e1615a61b3b1638659e6cae71)
    * re-ask surface for badly tagged tracks (by matkoniecz) (commit 94d444069bb4918b6b9a9248470ecd9825b3d14e)
    * add hide button (commit 6ca8a6d38ee0e408dc38cd52cdd213c333c251c6)
    * adjust dark theme (commit 9dbf3908180edcb30f1b0464a62f5c134a47c0b0)
    * move "no cycleway" answer to different position  (commit 1af6ce536211bd005a927414b372aa3c360f0620)
    * small fixes (commit b6daf8211e8f440a043ce179ec78cb9eef409939)

    * update 2d buildings to work from start (08a791676728d683849938f8a7c03a2bbee92131)
    * allow switching 3D buildings (825afd551347cb1add9446d72c5df896e98893e8)
    * add raised crossing answers to crossing type (5c9a07257d30184475d92df2908180ff0e159d9c)
    * add raised crossing answer to show poi quest, remove unused imports (cdf7fa5aaf05ac289cb356e314bbf5ffc5f6517f)

  Not wanted yet:
    * add button to reverse quest order (commit 1982e79ef618444bc41cb609f9d5270805a24ab2)
    * (preparing for westnordost presets) - add quest profiles (commit 3d97a952d355e2ef4b4ee3c6db0670cda60f5876)
    * auto-download only if auto-upload is allowed (commit 52cf846a7544c5c1fbf39f1ef0cae8eb93b945ab)
    * add optional zoom with volume buttons (commit 56466f34760db956fd385eef700553f2a3013003)
    * add gpx notes (commit a01bc6a2ba54fca1c791237571d6b08a4f1ceb2e) -- BUGGY on -dev version no pictures, no easy sharing of notes

* allow house address quest even if no building type ?
  See [issue #2464](https://github.com/streetcomplete/StreetComplete/issues/2464)
  See also https://github.com/streetcomplete/StreetComplete/discussions/3558
  Or myself: allow house number even if building is not solved, with
  subanswers: "no, it's a garage" and "no, it's a shed" and "no, it's something else - leave a note")

* add language selector (or force language to HR in this fork if it is much simpler)
  See https://github.com/streetcomplete/StreetComplete/issues/2964#issuecomment-862609036 and related issues.
* check https://github.com/matkoniecz/Zazolc ?
* check https://github.com/streetcomplete/StreetComplete/compare/master...pietervdvn:master ?
* get icon for phone quest from atrate branch?
  to update `helium314` quest.
  Maybe even check `atrate` code for such quest and merge/replace?
* implement quest for air compressor
  https://github.com/streetcomplete/StreetComplete/issues/3053
* implement smoking quest
  https://github.com/streetcomplete/StreetComplete/issues/539#issuecomment-946343763
* house type quest, allow caching Residental/Commercial too
* Surface smoothness quest (upstream-smoothness branch)
  https://github.com/westnordost/StreetComplete/issues/1630
  https://github.com/streetcomplete/StreetComplete/pull/3257
* re-enable or manually hardcode quest ordering if it will be removed
  https://github.com/streetcomplete/StreetComplete/issues/3034#issuecomment-879866839
* enable pedastrian crossing checks on more ways?
  https://github.com/streetcomplete/StreetComplete/issues/398#issuecomment-869151327

* FIXME old branches to new branches
    * make a copy of mnalis-v38 branch, and squash all small things I want in one commit for easier handling
      (how exactly? "git cherry-pick -n" ?)
    * check other unread mails / open tabs
    * in final mnalis-with-everything branch (set as default branch): mnalis-v38 + many helium314 + smoothness
