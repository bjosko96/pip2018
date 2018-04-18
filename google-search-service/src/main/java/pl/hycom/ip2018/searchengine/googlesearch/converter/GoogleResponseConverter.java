package pl.hycom.ip2018.searchengine.googlesearch.converter;

import org.springframework.core.convert.converter.Converter;
import pl.hycom.ip2018.searchengine.googlesearch.model.AbstractGoogleSearchResponse;
import pl.hycom.ip2018.searchengine.googlesearch.model.Result;
import pl.hycom.ip2018.searchengine.googlesearch.googlemodel.GoogleMetaTag;
import pl.hycom.ip2018.searchengine.googlesearch.googlemodel.GooglePageMap;
import pl.hycom.ip2018.searchengine.googlesearch.googlemodel.GoogleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoogleResponseConverter implements Converter<GoogleResponse, AbstractGoogleSearchResponse> {

    @Override
    public AbstractGoogleSearchResponse convert(GoogleResponse googleResponse) {
        AbstractGoogleSearchResponse result = new AbstractGoogleSearchResponse();
        List<Result> simpleItems = new ArrayList<>();

        Optional.ofNullable(googleResponse)
                .map(GoogleResponse::getResults)
                .ifPresent(googleItems -> googleItems
                        .forEach(googleItem -> {
                            Result item = new Result();
                            item.setHeader(googleItem.getHeader());
                            item.setSnippet(googleItem.getSnippet());
                            item.setTimestamp(Optional.ofNullable(googleItem.getPageMap())
                                    .map(GooglePageMap::getMetaTags)
                                    .map(googleMetaTags -> googleMetaTags.get(0))
                                    .map(GoogleMetaTag::getTimestamp).orElse(null));
                            item.setUrl(googleItem.getUrl());
                            simpleItems.add(item);
                        }));

        result.setResults(simpleItems);
        return result;
    }
}
