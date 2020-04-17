package com.filmi3k.movies.controllers;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController
{

    private static final String REDIRECT = "redirect:";

    protected ModelAndView view(String view, ModelAndView modelAndView)
    {
        modelAndView.setViewName(view);

        return modelAndView;
    }

    protected ModelAndView view(String view)
    {
        return this.view(view, new ModelAndView());
    }

    protected ModelAndView redirect(String url)
    {
        return this.view(REDIRECT + url);
    }
}
