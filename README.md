<p align="center">
<img src="https://isitmaintained.com/badge/resolution/Berehum/teacups.svg">
<img src="https://isitmaintained.com/badge/open/Berehum/teacups.svg">
<h1 align="center">Teacups</h1>
</p>

#### Quick Navigation

- [Downloads / Building](#-downloads--building)
- [Contributors](#-contributors)
- [Support](#-support)
- [License](#-license)
- [Wiki](#-wiki)

## 📖 Documentation / Wiki

Teacups has a [wiki](https://github.com/Berehum/teacups/wiki/) that contains the most important aspects of the plugin.
Including the layout of the config files and developer API.

#### 🌟 Highlighted Articles

- [FAQs](https://github.com/Berehum/teacups/wiki)
- [Configuration](https://github.com/Berehum/teacups/wiki/Config)
- [Teacup File](https://github.com/Berehum/teacups/wiki/Teacup-File)
- [Show File](https://github.com/Berehum/teacups/wiki/Show-File)

## 💡 Support

For support, please open a [GitHub issue](https://github.com/Berehum/teacups/issues). Please follow the layout
in [CONTRIBUTING.md](https://github.com/Berehum/teacups/blob/master/CONTRIBUTING.md).

#### 🌐 Language

Please speak English and do not use any vulgar or harmful language. We work on this project in our free time, getting
mad at us, making demands, or just complaining in general will not achieve anything.

## 💾 Downloads / Building

The latest release version of teacups can be found on [Spigot](https://www.spigotmc.org/resources/). The latest build of
teacups (development version)
can be found on [GitHub](https://github.com/Berehum/teacups/actions).

Alternatively, you can build Teacups via Maven. Release versions of Teacups are built using **Java 8**, you can change
the target version in ``pom.xml``.

* ensure Java 8 is installed on your machine
* clone this repository
* run ``mvn clean install`` in the base directory

The final jar will be in the `target/` directory.

#### Dependencies

Teacups has [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) as a dependency. You **must** install
ProtocolLib version **4.7.0 or higher**.

#### 🧰 Developer API

Unfortunately, there is not a fancy developer API yet. However, you are able to do some things with the current code,
such as [creating new show actions](https://github.com/LMBishop/teacups/wiki/New-Show-Action).

This project currently doesn't have a repository, so you'll have to download a copy of the plugin and add it to your
package managing software yourself. Sorry :(

## 👫 Contributors

Click [here](https://github.com/Berehum/teacups/graphs/contributors) to see a list of contributors.

#### 🤝 Do you want to contribute?

Please read  [CONTRIBUTING.md](https://github.com/Berehum/teacups/blob/master/CONTRIBUTING.md) for more information.

## 🙌 Credits

### Cloud command framework

Credits to @Incendo for the Cloud command framework that is used in this project.

### ProtocolLib (wrappers)

Credits to @dmulloy2 and @aandk for ProtocolLib, the library that simplifies working with packets a lot. Also a big
thanks to them for the wrappers of the packets.

## 📜 License

The **source code** for Teacups is licensed under the GNU General Public License v3.0, to view the license
click [here](https://github.com/Berehum/teacups/blob/master/LICENSE.txt).

The **artwork** for Teacups is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0
International License ![](https://i.creativecommons.org/l/by-nc-sa/4.0/80x15.png), to learn more
click [here](https://creativecommons.org/licenses/by-nc-sa/4.0/).
