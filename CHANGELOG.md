## 0.10.0 / 2020 Jan 25
> Styled the site a little more nicely with the help of Bulma CSS.

## 0.9.1 / 2019 Dec 24
> Fixed the mobile styles

## v0.9.0 / 2019 Dec 24
> Switched the website over to a simple static HTML/CSS site. Figured this
> is something I can update long-term.

## v0.8.0 / 2017 Dec 22

> This release adds a spinner icon while the form submit is being processed.

* **Add** - added a spinner icon while the contact form email is being sent

```clojure
[ilyab "0.7.0"]
```

## v0.7.0 / 2017 Dec 22

> This release adds front-end form validation for the contact form. We require
> both the subject and body and show error feedback when either isn't filled in.

* **Add** - added front-end contact from validation

```clojure
[ilyab "0.7.0"]
```

## v0.6.0 / 2017 Dec 19

> This release changes the "send" button to "sending..." on the contact form while
> AWS is doing its thing sending the email. This makes the submit feel less choppy.

* **Update** - show "sending..." while the contact form is being handled

```clojure
[ilyab "0.6.0"]
```

## v0.5.0 / 2017 Dec 15

> This release gets contact form working. Submissions are now sent via SES to my
> email address. There's no front-end validation of the form yet, but this is good
> enough for now.

* **Update** - email contact form submissions via SES

```clojure
[ilyab "0.5.0"]
```

## v0.4.0 / 2017 Dec 11

> This release upgrades to Font Awesome 5.

* **Update** - upgraded to Font Awesome v5.0.1
* **Update** - renamed the FA color styles so that they don't need to change on each upgrade
* **Update** - added a transition to the icon color change on hover

```clojure
[ilyab "0.4.0"]
```

## v0.3.1 / 2017 Dec 7

> This release adds adds a bit of top margin on the footer because it was overalapping
> the form on mobile.

* **Fix** - fixed the margin on the footer

```clojure
[ilyab "0.3.1"]
```

## v0.3.0 / 2017 Dec 5

> This release adds the GitHub and LinkedIn icons and moves the subtitle to the
> footer.

* **Add** - added GitHub, LinkedIn, and source icons
* **Update** - moved the subtitle to the footer

```clojure
[ilyab "0.3.0"]
```

## v0.2.0 / 2017 Nov 13

> This release adds basic email sending. We take the subject and body submitted
> on the contact form and send it off in an email.

* **Add** - email the data submitted via `POST /v1/contact`

```clojure
[ilyab "0.2.0"]
```


## v0.1.0 / 2017 Nov 11

> This is the initial release of the `ilyab` website and service.

* **New** - everything - it's brand new

```clojure
[ilyab "0.1.0"]
```
