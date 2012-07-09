function mat = imgpca(img)
     r = img(:,:,1);
     g = img(:,:,2);
     b = img(:,:,3);
     in = double([r(:),g(:),b(:)]');
     mu = mean(in, 2);
     X = bsxfun(@minus, in, mu);
     [U, S] = svd(X, 0);
     mat = U';
end